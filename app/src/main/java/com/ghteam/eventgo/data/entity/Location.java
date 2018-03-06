package com.ghteam.eventgo.data.entity;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Created by nikit on 04.12.2017.
 */


public class Location extends RealmObject {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}


