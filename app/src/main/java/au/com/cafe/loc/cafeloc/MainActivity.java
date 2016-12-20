package au.com.cafe.loc.cafeloc;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import au.com.cafe.loc.cafeloc.network.dto.ExploreVenueDto;
import au.com.cafe.loc.cafeloc.network.handlers.ExploreVenueNetworkHandler;
import au.com.cafe.loc.cafeloc.util.LocationService;
import au.com.cafe.loc.cafeloc.util.PermissionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LocationService.LocationServiceListener {

    @BindView(R.id.button_grant_access)
    Button mButtonGrantAccess;
    ProgressDialog mProgressDialog;

    LocationService mLocationService;

    private final int CODE_PERM_LOC = 100;
    private final String TAG = MainActivity.class.getSimpleName();
    private String longitude;
    private String latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mButtonGrantAccess.setOnClickListener(this);
        mLocationService = new LocationService(this, this);

        if (PermissionManager.checkPermissions(this, PermissionManager.getLocationPermissions())) {
            mLocationService.fetchLocation();
            mProgressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.fetching_location), true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationService != null) {
            mLocationService.stopLocationListener();
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
                mLocationService.fetchLocation();
                mProgressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.fetching_location), true);
            }
        }
    }

    private void requestForService() {

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
            public void onVenueDetailsFetched(ExploreVenueDto exploreVenueDto) {
                mProgressDialog.dismiss();
            }

            @Override
            public void onError(String errorReason) {
                mProgressDialog.dismiss();
            }
        });


    }

    @Override
    public void onLocationUpdate(double lat, double lon) {
        Log.d(TAG, "Latitude: " + lat);
        Log.d(TAG, "Longitude: " + lon);
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lon);
        mProgressDialog.setTitle(getString(R.string.app_name));
        mProgressDialog.setMessage(getString(R.string.fetching_details_pls_wait));
        requestForService();
    }

    @Override
    public void onLocationError(String providerError) {
        mProgressDialog.dismiss();
        Toast.makeText(this, providerError, Toast.LENGTH_LONG).show();
    }
}
