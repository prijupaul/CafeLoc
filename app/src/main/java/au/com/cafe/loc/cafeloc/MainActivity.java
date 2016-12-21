package au.com.cafe.loc.cafeloc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import au.com.cafe.loc.cafeloc.adapters.ExploreVenueAdapter;
import au.com.cafe.loc.cafeloc.network.dto.ExploreVenueDto;
import au.com.cafe.loc.cafeloc.network.handlers.ExploreVenueNetworkHandler;
import au.com.cafe.loc.cafeloc.util.ExecutorUtils;
import au.com.cafe.loc.cafeloc.util.LocationService;
import au.com.cafe.loc.cafeloc.util.NetworkUtil;
import au.com.cafe.loc.cafeloc.util.PermissionManager;
import au.com.cafe.loc.cafeloc.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LocationService.LocationServiceListener, ExploreVenueAdapter.ExploreVenueAdapterListener {

    @BindView(R.id.button_grant_access)
    Button mButtonGrantAccess;
    @BindView(R.id.ll_location_hint)
    LinearLayout mLLLocationHint;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ProgressDialog mProgressDialog;
    private LocationService mLocationService;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExploreVenueAdapter mExploreVenueAdapter;

    private final int CODE_PERM_LOC = 100;
    private final String TAG = MainActivity.class.getSimpleName();
    private String longitude;
    private String latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPreferenceManager.setFilePath(this);
        mButtonGrantAccess.setOnClickListener(this);
        mLocationService = new LocationService(this, this);
        mExploreVenueAdapter = new ExploreVenueAdapter();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mExploreVenueAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionManager.checkPermissions(this, PermissionManager.getLocationPermissions())) {
            latitude = SharedPreferenceManager.getLatitude(getApplicationContext());
            longitude = SharedPreferenceManager.getLongitude(getApplicationContext());

            // Dont show the progress dialog is the location details are fetched previously.
            if(latitude.isEmpty() || longitude.isEmpty()) {
                mProgressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.fetching_location), true);
            }
            if(!mLocationService.isLocationServiceStarted()) {
                mLocationService.fetchLocation();
                mLocationService.setListener(this);
                mLLLocationHint.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
        else {
            mLLLocationHint.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationService != null) {
            mLocationService.stopLocationListener();
        }

        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_grant_access: {
                ActivityCompat.requestPermissions(this, PermissionManager.getLocationPermissions(), CODE_PERM_LOC);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODE_PERM_LOC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mLLLocationHint.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                mLocationService.fetchLocation();
                if( (mProgressDialog != null) && !mProgressDialog.isShowing()) {
                    mProgressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.fetching_location), true);
                }
            }
        }
    }

    private void requestForService() {

        if(!NetworkUtil.IsNetworkConnectionAvailable(this)){
            Toast.makeText(this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        ExploreVenueNetworkHandler exploreVenueNetworkHandler = new ExploreVenueNetworkHandler.ExploreVenueBuilder()
                .setClientId(BuildConfig.FOURSQUARE_CLIENT_ID)
                .setClientSecret(BuildConfig.FOURSQUARE_CLIENT_SECRET)
                .setSection("coffee")
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setSortByDistance(true)
                .build(getApplicationContext());
        exploreVenueNetworkHandler.getPopularVenues(new ExploreVenueNetworkHandler.ExploreVenueNetworkHandlerListener() {
            @Override
            public void onVenueDetailsFetched(final ExploreVenueDto exploreVenueDto) {
                if(mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }

                ExecutorUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ExploreVenueDto.items> itemsArrayList = exploreVenueDto.getResponse().getGroupsArrayList().get(0).getItemsArrayList();
                        exploreVenueDto.sortOnDistance(itemsArrayList);
                        mExploreVenueAdapter.setExploreVenueDto(itemsArrayList,MainActivity.this);
                        mExploreVenueAdapter.notifyDataSetChanged();
                    }
                },1);

            }

            @Override
            public void onError(String errorReason) {
                if(mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });


    }

    @Override
    public void onLocationUpdate(double lat, double lon) {

        if(!latitude.isEmpty() &&  !longitude.isEmpty()) {

            Location targetLocation = new Location("");
            targetLocation.setLatitude(lat);
            targetLocation.setLongitude(lon);

            Location currentLocation = new Location("");
            currentLocation.setLatitude(Double.valueOf(latitude));
            currentLocation.setLongitude(Double.valueOf(longitude));

            float inMeters = currentLocation.distanceTo(targetLocation);
            if((inMeters < LocationService.MIN_DISTANCE_CHANGE_FOR_UPDATES) &&
                    (mRecyclerView.getAdapter().getItemCount() > 0)){
                return;
            }
        }

        latitude = String.valueOf(lat);
        longitude = String.valueOf(lon);


        SharedPreferenceManager.saveLatitude(latitude,getApplicationContext());
        SharedPreferenceManager.saveLongitude(longitude,getApplicationContext());

        if(mProgressDialog != null) {
            mProgressDialog.setTitle(getString(R.string.app_name));
            mProgressDialog.setMessage(getString(R.string.fetching_details_pls_wait));
        }
        mLLLocationHint.setVisibility(View.GONE);
        requestForService();
    }

    @Override
    public void onLocationError(String providerError) {
        mProgressDialog.dismiss();
        if(latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(this, providerError, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view, String url) {
        Intent webViewIntent = new Intent(this,WebActivity.class);
        webViewIntent.putExtra(WebActivity.BUNDLE_URL_ID,url);
        startActivity(webViewIntent);
    }

    @Override
    public void onLoadMaps(String lat, String lon) {

        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s,%s", lat, lon);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}
