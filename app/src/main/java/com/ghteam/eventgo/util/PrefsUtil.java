package com.ghteam.eventgo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nikit on 25.11.2017.
 */

public class PrefsUtil {


    private static SharedPreferences mPreferences;
    private static Context mContext;

    public static final String PREFS_LOGGED_TYPE = "logged_type";

    public static final String LOGGED_TYPE_NONE = "logged_type_none";
    public static final String LOGGED_TYPE_EMAIL = "logged_type_emil";
    public static final String LOGGED_TYPE_FACEBOOK = "logged_type_facebook";

    public static final String LAST_KNOWN_LATITUDE = "last_known_latitude";
    public static final String LAST_KNOWN_LONGITUDE = "last_known_longitude";

    public static final String USER_PROFILE_PICTURE = "user_profile_picture";
    public static final String USER_DISPLAY_NAME = "user_display_name";

    private PrefsUtil() {
    }

    public static void init(Context context) {
        mPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        mContext = context.getApplicationContext();
    }

    public static void setLoggedType(String loggedType) {
        putString(PREFS_LOGGED_TYPE, loggedType);
    }

    public static void setUserProfilePicture(String url) {
        putString(USER_PROFILE_PICTURE, url);
    }

    public static String getUserProfilePicture() {
        return getString(USER_PROFILE_PICTURE, mContext.getResources().getString(R.string.def_profile_picture));
    }

    public static void setUserDisplayName(String displayName) {
        putString(USER_DISPLAY_NAME, displayName);
    }

    public static String getUserDisplayName(){
        return getString(USER_DISPLAY_NAME);
    }

    public static void setLastKnownLocation(LatLng location) {
        mPreferences.edit().putFloat(LAST_KNOWN_LATITUDE, (float) location.latitude).apply();
        mPreferences.edit().putFloat(LAST_KNOWN_LONGITUDE, (float) location.longitude).apply();
    }

    @Nullable
    public static LatLng getLastKnownLocation() {
        float latitude = getFloat(LAST_KNOWN_LATITUDE, 200);
        float longitude = getFloat(LAST_KNOWN_LONGITUDE, 200);

        if (latitude == 200 || longitude == 200) {
            return null;
        } else {
            return new LatLng(latitude, longitude);
        }
    }

    public static String getLoggedType() {
        return getString(PREFS_LOGGED_TYPE, LOGGED_TYPE_NONE);
    }

    private static void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    private static float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    private static String getString(String key) {
        return mPreferences.getString(key, "");
    }

    private static String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

}
