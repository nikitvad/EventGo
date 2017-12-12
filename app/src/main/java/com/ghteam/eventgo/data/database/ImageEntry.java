package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.model.Event;

/**
 * Created by nikit on 12.12.2017.
 */

@Entity(foreignKeys = @ForeignKey(
        entity = Event.class,
        parentColumns = "id",
        childColumns = "ownerId"
), tableName = "images")

public class ImageEntry {

    public int ownerId;

    @NonNull
    @PrimaryKey
    public String url;
}
