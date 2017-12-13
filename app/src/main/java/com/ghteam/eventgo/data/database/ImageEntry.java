package com.ghteam.eventgo.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.ghteam.eventgo.data.entity.Event;

/**
 * Created by nikit on 12.12.2017.
 */

@Entity(foreignKeys = @ForeignKey(
        entity = Event.class,
        parentColumns = "id",
        childColumns = "ownerId"
), tableName = "images")

public class ImageEntry {

    public String ownerId;


    @PrimaryKey(autoGenerate = true)
    public int id;

    public String url;

    public ImageEntry() {
    }

    public ImageEntry(String eventId, String url) {
        this.ownerId = eventId;
        this.url = url;
    }


}
