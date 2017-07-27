package com.example.pankaj.trackpath;

/**
 * Created by pankaj on 7/22/17.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL = 5000;
    public static final long FASTEST_UPDATE = UPDATE_INTERVAL / 2;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    public static Location mCurrentLocation;
    String mLastUpdateTime;
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    public static ArrayList<LatLng> listCoord;
    public static Boolean isConnected;


    @Override
    public void onCreate() {
        super.onCreate();
        mIntentService = new Intent(this, LocationUpdates.class);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
//
//        SettingsClient client = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                // All location settings are satisfied. The client can initialize
//                // location requests here.
//                // ...
//            }
//        });
//
//        task.addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                int statusCode = ((ApiException) e).getStatusCode();
//                switch (statusCode) {
//                    case CommonStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            ResolvableApiException resolvable = (ResolvableApiException) e;
//                            resolvable.startResolutionForResult(MainActivity.this,
//                                    REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException sendEx) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way
//                        // to fix the settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
        listCoord = new ArrayList<>();
//        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        buildGoogleApiClient();
    }

    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocationService getServerInstance() {
            return LocationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (mGoogleApiClient.isConnected()) {
            return START_STICKY;
        }
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Set Location Permissions and Restart the App", Toast.LENGTH_LONG).show();
//            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        listCoord = new ArrayList<>();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mPendingIntent);

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
//        mCurrentLocation = location;
//        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//        addMarker();
        listCoord.add(new LatLng(location.getLatitude(), location.getLongitude()));
//        MapsActivity.drawMarker(new LatLng(location.getLatitude(), location.getLongitude()));
//            Log.("LL - > " + listCoord);
        Toast.makeText(this, "Lat:" +location.getLatitude()+" Long: "+ location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Location Service", "Connection failed: " + result.getErrorCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }



}