package com.ghteam.eventgo.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ghteam.eventgo.data.entity.Category;

/**
 * Created by nikit on 12.12.2017.
 */
@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM categories")
    void deleteAll();

    @Query("SELECT * FROM categories WHERE ownerId LIKE :eventId")
    LiveData<Category> findCategoryByOwner(String eventId);
}
