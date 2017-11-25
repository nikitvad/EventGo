package com.ghteam.eventgo;

import android.app.Application;

import com.ghteam.eventgo.util.PrefsUtil;

/**
 * Created by nikit on 25.11.2017.
 */

public class EventGoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PrefsUtil.init(getApplicationContext());
    }
}
