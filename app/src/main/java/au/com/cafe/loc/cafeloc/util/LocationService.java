
package au.com.cafe.loc.cafeloc.util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import au.com.cafe.loc.cafeloc.R;

import java.security.Provider;


/**
 * Created by priju.jacobpaul on 21/12/16.
 */
public class LocationService extends Service {

    public interface LocationServiceListener{
        void onLocationUpdate(double lat,double lon);
        void onLocationError(String providerError);
    }

    private final Context mContext;

    static double latitude; // latitude
    static double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected GetFromLocationManager locationManagerProvider;

    private GooglePlayLocationProvider playLocationProvider;

    private LocationServiceListener locationServiceListener;

    public LocationService(Context context,LocationServiceListener locationServiceListener) {
        this.mContext = context;
        this.locationServiceListener = locationServiceListener;

        playLocationProvider = new GooglePlayLocationProvider(context);
        locationManagerProvider = new GetFromLocationManager(context);
    }

    public void fetchLocation(){
        locationManagerProvider.getLocation();
        playLocationProvider.getLastKnownLocation();
    }


    /**
     * Stop using GPS listener Calling this function will stop using GPS in your app
     */
    public void stopLocationListener() {

        stopSelf();

        if (!isPermissionAvailable()) {
            return;
        }

        try {
            if (locationManagerProvider != null) {
                locationManagerProvider.stopLocationManager();
            }

            if (playLocationProvider != null) {
                playLocationProvider.stopApiClient();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private boolean isPermissionAvailable() {

        if (!PermissionManager.checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ||
                !PermissionManager.checkPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return false;
        }
        return true;
    }

    private class GetFromLocationManager implements LocationListener {

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Context mContext;

        LocationManager locationManager;

        Location location;

        LocationServiceListener locationServiceListener;

        public GetFromLocationManager(Context context) {
            mContext = context;
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(locationServiceListener != null) {
                locationServiceListener.onLocationUpdate(latitude, longitude);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if(locationServiceListener != null) {
                locationServiceListener.onLocationError("Please enable " + provider + "for better results.");
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }


        private void populateLastKnownDetails() {

            if (!isPermissionAvailable()) {
                return;
            }

            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        public void stopLocationManager() {

            if (!isPermissionAvailable()) {
                return;
            }

            if (locationManager != null) {
                locationManager.removeUpdates(GetFromLocationManager.this);
            }
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        public Location getLocation() {

            if (!isPermissionAvailable()) {
                return null;
            }

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                populateLastKnownDetails();

                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    locationServiceListener.onLocationError("For better results please enable location");
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {

                        locationManager.requestLocationUpdates(
                                LocationManager.PASSIVE_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                locationServiceListener.onLocationUpdate(latitude,longitude);
                            }
                            else {
                                locationServiceListener.onLocationError(mContext.getString(R.string.location_not_available));
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    else if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    locationServiceListener.onLocationUpdate(latitude,longitude);
                                }
                                else {
                                    locationServiceListener.onLocationError(mContext.getString(R.string.location_not_available));
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

    }

    private class GooglePlayLocationProvider implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener

    {

        private GoogleApiClient mGoogleApiClient;
        private Context mContext;
        private Location mLastLocation;

        public GooglePlayLocationProvider(Context context) {
            mContext = context;
            setupApiClient();
        }

        private void setupApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        public void getLastKnownLocation() {
            mGoogleApiClient.connect();

        }

        public void stopApiClient() {

            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            if (!isPermissionAvailable()) {
                return;
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                locationServiceListener.onLocationUpdate(latitude,longitude);
            }
            else {
                locationServiceListener.onLocationError(mContext.getString(R.string.location_not_available));
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            locationServiceListener.onLocationError(connectionResult.getErrorMessage());
        }


    }


}
