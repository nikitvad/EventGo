package com.ghteam.eventgo.data.entity;

import com.ghteam.eventgo.util.network.LocationUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikit on 07.03.2018.
 */

public class UserLocationInfo {
    private String userId;
    private String userDisplayName;

    private Location location;

    private Date date;

    private long serializedLocation;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getSerializedLocation() {
        return serializedLocation;
    }

    public void setSerializedLocation(long serializedLocation) {
        this.serializedLocation = serializedLocation;
    }

    @Override
    public String toString() {
        return "UserLocationInfo{" +
                "userId='" + userId + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", location=" + location +
                ", serializedLocation=" + serializedLocation +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("userDisplayName", userDisplayName);
        result.put("date", date);
        result.put("location", location.toMap());

        serializedLocation = LocationUtil.serializeLatLong(location.getLatitude(), location.getLongitude());

        result.put("serializedLocation", serializedLocation);

        return result;
    }
}
