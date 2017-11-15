package com.ghteam.eventgo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nikit on 15.11.2017.
 */

public class PreferencesUtil {

    private static Context mContext;
    private static SharedPreferences preferences;

    public static void init(Context context){
        mContext = context;
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }






}
