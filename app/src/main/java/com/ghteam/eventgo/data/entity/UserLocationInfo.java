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
    private String profileImageUrl;

    private AppLocation appLocation;

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

    public AppLocation getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(AppLocation appLocation) {
        this.appLocation = appLocation;
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
                ", appLocation=" + appLocation +
                ", serializedLocation=" + serializedLocation +
                '}';
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLocationInfo that = (UserLocationInfo) o;

        if (serializedLocation != that.serializedLocation) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (userDisplayName != null ? !userDisplayName.equals(that.userDisplayName) : that.userDisplayName != null)
            return false;
        if (profileImageUrl != null ? !profileImageUrl.equals(that.profileImageUrl) : that.profileImageUrl != null)
            return false;
        if (appLocation != null ? !appLocation.equals(that.appLocation) : that.appLocation != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userDisplayName != null ? userDisplayName.hashCode() : 0);
        result = 31 * result + (profileImageUrl != null ? profileImageUrl.hashCode() : 0);
        result = 31 * result + (appLocation != null ? appLocation.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (serializedLocation ^ (serializedLocation >>> 32));
        return result;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("profileImageUrl", profileImageUrl);
        result.put("userDisplayName", userDisplayName);
        result.put("date", date);
        result.put("appLocation", appLocation.toMap());

        serializedLocation = LocationUtil.serializeLatLong(appLocation.getLatitude(), appLocation.getLongitude());

        result.put("serializedLocation", serializedLocation);

        return result;
    }
}
