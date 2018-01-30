package com.ghteam.eventgo.data.entity;

import io.realm.RealmObject;

/**
 * Created by nikit on 04.12.2017.
 */


public class Location extends RealmObject {
//    @PrimaryKey(autoGenerate = true)
//    public int id;

    public int ownerId;

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
}
