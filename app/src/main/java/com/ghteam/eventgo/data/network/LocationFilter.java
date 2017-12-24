package com.ghteam.eventgo.data.network;

import com.ghteam.eventgo.data.entity.Location;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nikit on 23.12.2017.
 */

public class LocationFilter {

    private LatLng location;
    private double height;
    private double width;


    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
