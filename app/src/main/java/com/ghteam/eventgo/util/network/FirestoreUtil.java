package com.ghteam.eventgo.util.network;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by nikit on 04.01.2018.
 */

public class FirestoreUtil {

    private static final String STR_EVENTS = "events";
    private static final String STR_EVENT_CATEGORIES = "event_categories";
    private static final String STR_USERS = "users";
    private static final String STR_DISCUSSIONS = "event_discussions";
    private static final String SRT_MY_EVENTS = "my_events";
    private static final String SRT_INTERESTED_EVENTS = "my_interested_events";
    private static final String SRT_GOING_EVENTS = "my_going_events";
    private static final String SRT_USER_LOCATION_INFO = "user_location_info";

    public static CollectionReference getReferenceToEvents() {
        return FirebaseFirestore.getInstance().collection(STR_EVENTS);
    }

    public static CollectionReference getReferenceToCategories() {
        return FirebaseFirestore.getInstance().collection(STR_EVENT_CATEGORIES);
    }

    public static CollectionReference getReferenceToUsers() {
        return FirebaseFirestore.getInstance().collection(STR_USERS);
    }

    public static CollectionReference getReferenceToDiscussions() {
        return FirebaseFirestore.getInstance().collection(STR_DISCUSSIONS);
    }

    public static CollectionReference getReferenceToUsersEvents(String uid) {
        return getReferenceToUsers().document(uid).collection(SRT_MY_EVENTS);
    }

    public static CollectionReference getReferenceToUserInterestedEvents(String uid) {
        return getReferenceToUsers().document(uid).collection(SRT_INTERESTED_EVENTS);
    }

    public static CollectionReference getReferenceToUserGoingEvents(String uid) {
        return getReferenceToUsers().document(uid).collection(SRT_GOING_EVENTS);
    }

    public static CollectionReference getReferenceToUserLocationInfo() {
        return FirebaseFirestore.getInstance().collection(SRT_USER_LOCATION_INFO);
    }

}
