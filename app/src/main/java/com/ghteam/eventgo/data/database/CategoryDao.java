package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.ghteam.eventgo.data.model.Category;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface CategoryDao {
    @Insert
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM categories WHERE ownerId LIKE :eventId")
    Category findCategory(int eventId);
}
