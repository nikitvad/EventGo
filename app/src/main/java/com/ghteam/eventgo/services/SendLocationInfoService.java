package com.ghteam.eventgo.services;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.entity.UserLocationInfo;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.CollectionReference;

import java.util.Date;

public class SendLocationInfoService extends IntentService {

    private static final String ACTION_SEND_LOCATION_INFO = "com.ghteam.eventgo.services.action.SEND_LOCATION_INFO";

    public SendLocationInfoService() {
        super("SendLocationInfoService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendUserLocationInfo(Context context) {
        Intent intent = new Intent(context, SendLocationInfoService.class);
        intent.setAction(ACTION_SEND_LOCATION_INFO);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_LOCATION_INFO.equals(action)) {
                handleActionSendUserLocationInfo();
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            CollectionReference collectionReference = FirestoreUtil.getReferenceToUserLocationInfo();

            UserLocationInfo userLocationInfo = new UserLocationInfo();

            AppLocation userAppLocation = new AppLocation();

            userAppLocation.setLatitude(location.getLatitude());
            userAppLocation.setLongitude(location.getLongitude());

            Log.d("sadfasdfasdf", "onLocationChanged: " + location.toString());

            userLocationInfo.setAppLocation(userAppLocation);
            userLocationInfo.setUserDisplayName(PrefsUtil.getUserDisplayName());
            userLocationInfo.setUserId(PrefsUtil.getUserId());
            userLocationInfo.setDate(new Date());

            collectionReference.document(PrefsUtil.getUserId()).set(userLocationInfo.toMap());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void handleActionSendUserLocationInfo() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        CollectionReference collectionReference = FirestoreUtil.getReferenceToUserLocationInfo();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            UserLocationInfo userLocationInfo = new UserLocationInfo();

            AppLocation userAppLocation = new AppLocation();

            userAppLocation.setLatitude(location.getLatitude());
            userAppLocation.setLongitude(location.getLongitude());

            Log.d("sadfasdfasdf", "onLocationChanged: " + location.toString());

            userLocationInfo.setAppLocation(userAppLocation);
            userLocationInfo.setUserDisplayName(PrefsUtil.getUserDisplayName());
            userLocationInfo.setUserId(PrefsUtil.getUserId());
            userLocationInfo.setDate(new Date());

            collectionReference.document(PrefsUtil.getUserId()).set(userLocationInfo.toMap());


        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
