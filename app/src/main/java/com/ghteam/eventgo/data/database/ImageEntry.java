package com.ghteam.eventgo.data.database;

import com.ghteam.eventgo.data.entity.Event;

/**
 * Created by nikit on 12.12.2017.
 */


public class ImageEntry {

    public String ownerId;


    public String url;

    public ImageEntry() {
    }

    public ImageEntry(String eventId, String url) {
        this.ownerId = eventId;
        this.url = url;
    }


}
