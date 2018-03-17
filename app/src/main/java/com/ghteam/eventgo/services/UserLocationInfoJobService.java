package com.ghteam.eventgo.services;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
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

/**
 * Created by nikit on 07.03.2018.
 */

public class UserLocationInfoJobService extends JobService {
    private static final String TAG = UserLocationInfoJobService.class.getSimpleName();

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

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("sadfasdfasdf", "onStartJob: ");
//        SendLocationInfoService.startActionSendUserLocationInfo(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        CollectionReference collectionReference = FirestoreUtil.getReferenceToUserLocationInfo();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);

        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
