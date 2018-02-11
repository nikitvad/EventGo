package com.ghteam.eventgo.data.database;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by nikit on 01.02.2018.
 */

public class DatabaseMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema realmSchema = realm.getSchema();

        if (oldVersion == 1) {
            realmSchema.get("Event")
                    .addField("discussionId", String.class);

            realmSchema.create("DiscussionMessage")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("ownerId", String.class, FieldAttribute.REQUIRED)
                    .addField("date", Date.class)
                    .addField("message", String.class);

            oldVersion++;
        }

        if (oldVersion == 2) {
            realmSchema.get("Event")
                    .addField("isDiscussionEnabled", boolean.class);

            oldVersion++;
        }

        if (oldVersion == 3) {
            realmSchema.remove("DiscussionMessage");
            oldVersion++;
        }

        if(oldVersion == 4){
            realmSchema.get("Event")
                    .addField("ownerName", String.class)
                    .addField("ownerProfilePicture", String.class)
                    .addField("interestedCount", int.class)
                    .addField("goingCount", int.class);
            oldVersion++;
        }


    }
}
