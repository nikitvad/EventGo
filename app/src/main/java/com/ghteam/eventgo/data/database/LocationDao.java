package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ghteam.eventgo.data.model.Location;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface LocationDao {
    @Insert
    void insertAll(Location... locations);

    @Delete
    void delete(Location location);

    @Query("DELETE FROM locations")
    void deleteAll();

    @Query("SELECT * FROM locations WHERE ownerId LIKE :eventId LIMIT 1")
    Location findLocationByEventId(int eventId);

}
