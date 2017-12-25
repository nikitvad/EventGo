package com.ghteam.eventgo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.ghteam.eventgo.util.PrefsUtil;

/**
 * Created by nikit on 25.11.2017.
 */

public class EventGoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PrefsUtil.init(getApplicationContext());

//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
    }
}
