package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ghteam.eventgo.data.model.Event;

import java.util.List;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface EventDao {

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM events")
    void deleteAll();

    @Query("SELECT * FROM events")
    List<Event> getAll();

    @Query("SELECT * FROM events WHERE id LIKE :id LIMIT 1")
    Event findEvent(int id);
}
