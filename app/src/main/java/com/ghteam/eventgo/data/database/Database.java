package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ghteam.eventgo.data.model.Category;
import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.data.model.Location;
/**
 * Created by nikit on 12.12.2017.
 */
@android.arch.persistence.room.Database(entities = {Event.class, ImageEntry.class,
        Category.class, Location.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static final String DATABASE_NAME = "event_go_database";

    private static volatile Database sInstance;

    public abstract EventDao eventDao();

    public abstract CategoryDao categoryDao();

    public abstract ImageDao imageDao();

    public abstract LocationDao locationDao();

    public static Database getInstance(Context content) {
        if (sInstance == null) {
            synchronized (Database.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(content.getApplicationContext(),
                            Database.class, DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
