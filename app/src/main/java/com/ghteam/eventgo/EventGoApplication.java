package com.ghteam.eventgo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.ghteam.eventgo.data.database.DatabaseMigration;
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by nikit on 25.11.2017.
 */

public class EventGoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmDefConfiguration = new RealmConfiguration.Builder()
                .name("event_go.realm")
                .schemaVersion(5)
                .migration(new DatabaseMigration())
                .build();
        Realm.setDefaultConfiguration(realmDefConfiguration);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);


        PrefsUtil.init(getApplicationContext());

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
