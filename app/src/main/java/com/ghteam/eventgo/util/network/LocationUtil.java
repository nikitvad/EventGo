package com.ghteam.eventgo.util.network;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 21.12.2017.
 */

public class LocationUtil {

    private static Location lastKnownLocation;

    @SuppressLint("MissingPermission")
    @Nullable
    public static Location updateLastKnownLocation(LocationManager locationManager, @Nullable Date minDate) {
        List<String> providers = locationManager.getAllProviders();

        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;
        Location bestLocation = null;

        long minTime;

        if (minDate != null) {
            minTime = minDate.getTime();
        } else {
            minTime = 0;
        }

        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                long time = location.getTime();
                float accuracy = location.getAccuracy();

                if (time > minTime && accuracy < bestAccuracy) {
                    bestLocation = location;
                    bestTime = time;
                    bestAccuracy = accuracy;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                    bestLocation = location;
                    bestTime = time;
                }

            }
        }

        lastKnownLocation = bestLocation;
        return lastKnownLocation;
    }

    @Nullable
    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    private static double EARTH_RADIUS = 6378;

    public static LatLng[] calculateRectangle(LatLng center, double height, double width) {

        double kilometersPerDegreeOfLongitude = (2 * Math.PI / 360) * EARTH_RADIUS * Math.cos(center.latitude);
        double kilometersPerDegreeOfLatitude = (2 * Math.PI / 360) * EARTH_RADIUS;

        double leftTopLatitude = center.latitude + (height / 2) / kilometersPerDegreeOfLatitude;
        double leftTopLongitude = center.longitude - (width / 2) / kilometersPerDegreeOfLongitude;

        double rightBottomLatitude = center.latitude - (height / 2) / kilometersPerDegreeOfLatitude;
        double rightBottomLongitude = center.longitude + (width / 2) / kilometersPerDegreeOfLongitude;

        LatLng topLeft = new LatLng(leftTopLatitude, leftTopLongitude);
        LatLng bottomRight = new LatLng(rightBottomLatitude, rightBottomLongitude);

        return new LatLng[]{topLeft, bottomRight};
    }

    public static double calculateDistance(double latitude1, double longitude1,
                                           double latitude2, double longitude2) {

        double kilometersPerDegreeOfLongitude = (2 * Math.PI / 360) * EARTH_RADIUS * Math.cos(latitude1);
        double kilometersPerDegreeOfLatitude = (2 * Math.PI / 360) * EARTH_RADIUS;

        double longitudeCathetus = (longitude1 - longitude2) * kilometersPerDegreeOfLongitude;
        double latitudeCathetus = (latitude1 - latitude2) * kilometersPerDegreeOfLatitude;

        return Math.sqrt((longitudeCathetus * longitudeCathetus) + (latitudeCathetus * latitudeCathetus));
    }

    public static Long serializeLatLongV2(double latitude, double longitude) {

        Date date = new Date();
        String strLatRes = "";
        String strLongRes = "";

        if (latitude > 0) {
            strLatRes = "1";
        }

        if (longitude > 0) {
            strLongRes = strLongRes + "1";
        } else {
            strLongRes = strLongRes + "0";
        }

        if (longitude > 100 || longitude > -100) {
            strLongRes = strLongRes + "0";
        }

        if (longitude > 10 || longitude > -10) {
            strLongRes = strLongRes + "0";
        }

        String strLat = Double.toString(Math.abs(latitude)).replace(".", "") + "000000";
        String strLong = Double.toString(Math.abs(longitude)).replace(".", "") + "000000";

        String str = Double.toString(Math.abs(latitude));

        strLatRes = strLatRes + strLat.substring(0, 6);

        if (longitude > 100 || longitude < -100) {
            strLongRes = strLongRes + strLong.substring(0, 7);

        } else {
            strLongRes = strLongRes + strLong.substring(0, 6);
        }

        Date date1 = new Date();
//        Log.d("qwerqwer", "processing time v1: " + (date1.getTime() - date.getTime()));

        return Long.parseLong(strLatRes + strLongRes);
    }

    public static long serializeLatLong(LatLng latLng){
        return serializeLatLong(latLng.latitude, latLng.longitude);
    }

    public static long serializeLatLong(double latitude, double longitude) {

        Date date = new Date();

        long roundedNormalizedLongitude = Math.abs(Math.round(longitude * 100_000));
        long roundedNormalizedLatitude = Math.abs(Math.round(latitude * 100_000));

//        Log.d("qwerqwer", "processing time v2: " + roundedNormalizedLongitude );


        StringBuilder strLngResult = new StringBuilder(Long.toString(roundedNormalizedLongitude));
        StringBuilder strLatResult = new StringBuilder(Long.toString(roundedNormalizedLatitude));

//        Log.d("qwerqwer", "processing time v2: " + strLngResult );

        int lngResultLength = strLngResult.length();
        if (lngResultLength < 8) {
            for (int i = 0; i < 8 - lngResultLength; i++) {
                strLngResult.insert(0, 0);
//                Log.d("qwerqwer", "processing time v2: " + strLngResult);

            }
        }

        if (longitude > 0) {
            strLngResult.insert(0, 1);
        } else {
            strLngResult.insert(0, 0);
        }

        int latResultLength = strLatResult.length();
        if (latResultLength < 7) {
            for (int i = 0; i < 7 - latResultLength; i++){
                strLatResult.insert(0, 0);
            }
        }

        if(latitude > 0 ){
            strLatResult.insert(0, 1);
        }else{
            strLatResult.insert(0, 0);
        }

        long result = Long.parseLong(strLatResult.toString() + strLngResult.toString());


        Date date1 = new Date();

        return result;
    }


}
