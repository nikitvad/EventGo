package com.ghteam.eventgo.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by nikit on 04.12.2017.
 */

@Entity(tableName = "locations", foreignKeys = @ForeignKey(
        entity = Event.class,
        parentColumns = "id",
        childColumns = "ownerId"
))
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int ownerId;

    private double latitude;
    private double longitude;

    @Ignore
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
