package com.ghteam.eventgo.data_new.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ghteam.eventgo.data_new.entity.Category;
import com.ghteam.eventgo.data_new.entity.Event;
import com.ghteam.eventgo.data_new.entity.Location;

import java.util.Date;

/**
 * Created by nikit on 12.12.2017.
 */
@android.arch.persistence.room.Database(entities = {Event.class, ImageEntry.class,
        Category.class, Location.class}, version = 4)
@TypeConverters(Database.Converters.class)
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

    public static class Converters {
        @TypeConverter
        public Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public Long dateToTimestamp(Date date) {
            if (date == null) {
                return null;
            } else {
                return date.getTime();
            }
        }
    }
}
