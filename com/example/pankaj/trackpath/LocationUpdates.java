package com.example.pankaj.trackpath;

/**
 * Created by pankaj on 7/22/17.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderApi;


public class LocationUpdates extends IntentService {
    private String TAG = this.getClass().getSimpleName();
    public LocationUpdates() {
        super("TrackPath");
    }
    public LocationUpdates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        if(location !=null)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder noti = new NotificationCompat.Builder(this);
            noti.setContentTitle("TrackPath");
            noti.setContentText(location.getLatitude() + "," + location.getLongitude());
            noti.setSmallIcon(R.mipmap.ic_launcher);
            notificationManager.notify(231, noti.build());
        }
    }
}