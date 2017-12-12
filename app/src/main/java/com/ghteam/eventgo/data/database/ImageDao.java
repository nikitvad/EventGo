package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface ImageDao {

    @Insert
    void insertAll(ImageEntry... imageEntries);

    @Delete
    void delete(ImageEntry imageEntry);

    @Query("SELECT * FROM images WHERE ownerId LIKE :ownerId")
    List<ImageEntry> findImages(String ownerId);
}
