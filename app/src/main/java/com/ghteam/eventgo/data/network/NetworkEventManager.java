package com.ghteam.eventgo.data.network;

import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikit on 14.02.2018.
 */

public class NetworkEventManager {

    private CollectionReference usersInterestsRef;
    private CollectionReference usersGoingRef;

    private CollectionReference eventsReference;

    private String userId;

    public NetworkEventManager(String uid) {

        userId = uid;
        eventsReference = FirestoreUtil.getReferenceToEvents();

        usersGoingRef = FirestoreUtil.getReferenceToUserGoingEvents(uid);
        usersInterestsRef = FirestoreUtil.getReferenceToUserInterestedEvents(uid);
    }

    public void addEventToInterests(String eventId) {
        Map<String, String> refToEvent = new HashMap<>();
        refToEvent.put("eventId", eventId);
        usersInterestsRef.document(eventId).set(refToEvent);

        Map<String, String> refToUser = new HashMap<>();
        refToUser.put("userId", userId);

        eventsReference.document(eventId).collection("interested").document(userId).set(refToUser);
    }

    public void addEventToGoing(String eventId) {
        Map<String, String> map = new HashMap<>();
        map.put("eventId", eventId);
        usersGoingRef.document(eventId).set(map);

        Map<String, String> refToUser = new HashMap<>();
        refToUser.put("userId", userId);
        eventsReference.document(eventId).collection("going").document(userId).set(refToUser);
    }
}
