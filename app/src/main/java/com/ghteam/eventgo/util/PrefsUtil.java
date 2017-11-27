package com.ghteam.eventgo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nikit on 25.11.2017.
 */

public class PrefsUtil {


    private static SharedPreferences mPreferences;

    public static final String PREFS_LOGGED_TYPE = "logged_type";

    public static final String LOGGED_TYPE_NONE = "logged_type_none";
    public static final String LOGGED_TYPE_EMAIL = "logged_type_emil";
    public static final String LOGGED_TYPE_FACEBOOK = "logged_type_facebook";

    private PrefsUtil() {
    }

    public static void init(Context context) {
        mPreferences = context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setLoggedType(String loggedType) {
        putString(PREFS_LOGGED_TYPE, loggedType);
    }

    public static String getLoggedType() {
        return getString(PREFS_LOGGED_TYPE);
    }

    private static void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    private static String getString(String key) {
        return mPreferences.getString(key, LOGGED_TYPE_NONE);
    }

}
