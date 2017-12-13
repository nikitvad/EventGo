package com.ghteam.eventgo.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.util.LiveDataList;

import java.util.List;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Event> events);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM events")
    void deleteAll();

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM events WHERE id LIKE :id LIMIT 1")
    LiveData<Event> findEventById(String id);
}
