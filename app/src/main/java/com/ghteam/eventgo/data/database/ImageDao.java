package com.ghteam.eventgo.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ImageEntry... imageEntries);

    @Delete
    void delete(ImageEntry imageEntry);

    @Query("DELETE FROM images")
    void deleteAll();

    @Query("SELECT * FROM images WHERE ownerId LIKE :ownerId")
    LiveData<List<ImageEntry>> findImagesByOwner(String ownerId);
}
