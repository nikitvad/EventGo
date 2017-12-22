package com.ghteam.eventgo.util.network;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 21.12.2017.
 */

//if ((time > minTime && accuracy < bestAccuracy)) {
//        bestResult = location;
//        bestAccuracy = accuracy;
//        bestTime = time;
//        }
//        else if (time < minTime &&
//        bestAccuracy == Float.MAX_VALUE && time > bestTime){
//        bestResult = location;
//        bestTime = time;
//        }
//        }
public class LocationUtil {

    @SuppressLint("MissingPermission")
    @Nullable
    public static Location getLastKnownLocation(LocationManager locationManager, @Nullable Date minDate) {
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
        return bestLocation;
    }

}
