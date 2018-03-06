package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.task.TaskResultListener;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    public void addEventToInterests(Event event, final TaskResultListener<Boolean> resultListener) {
        if(event.getId()!=null && event.getId().length()>0) {

            usersInterestsRef.document(event.getId()).set(event.toMap());

            Map<String, String> refToUser = new HashMap<>();
            refToUser.put("userId", userId);

            eventsReference.document(event.getId()).collection("interested").document(userId).set(refToUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            resultListener.onResult(task.isSuccessful());
                        }
                    });
        }
    }

    public void removeEventFromInterested(String eventId, final TaskResultListener<Boolean> resultListener) {
        usersInterestsRef.document(eventId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resultListener.onResult(task.isSuccessful());
            }
        });

        eventsReference.document(eventId).collection("interested").document(userId).delete();
    }

    public void addEventToGoing(Event event, final TaskResultListener<Boolean> resultListener) {
        if (event.getId() != null && event.getId().length() > 0) {

            usersGoingRef.document(event.getId()).set(event.toMap());

            Map<String, String> refToUser = new HashMap<>();
            refToUser.put("userId", userId);
            eventsReference.document(event.getId()).collection("going").document(userId).set(refToUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            resultListener.onResult(task.isSuccessful());
                        }
                    });
        }
    }

    public void removeEventFromGoing(String eventId, final TaskResultListener<Boolean> resultListener) {
        usersGoingRef.document(eventId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                resultListener.onResult(task.isSuccessful());
            }
        });

        eventsReference.document(eventId).collection("going").document(userId).delete();
    }

    public void isUserInterestedEvent(String eventId, final TaskResultListener<Boolean> taskResultListener) {
        usersInterestsRef.document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    taskResultListener.onResult(true);
                } else {
                    taskResultListener.onResult(false);
                }
            }
        });

    }


}
