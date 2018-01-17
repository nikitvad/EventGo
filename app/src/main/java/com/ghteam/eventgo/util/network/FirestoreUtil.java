package com.ghteam.eventgo.util.network;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by nikit on 04.01.2018.
 */

public class FirestoreUtil {

    public static final String STR_EVENTS = "events";
    public static final String STR_EVENT_CATEGORIES = "event_categories";

    public static CollectionReference getReferenceToEvents() {
        return FirebaseFirestore.getInstance().collection(STR_EVENTS);
    }

    public static CollectionReference getReferenceToCategories() {
        return FirebaseFirestore.getInstance().collection(STR_EVENT_CATEGORIES);
    }
}
