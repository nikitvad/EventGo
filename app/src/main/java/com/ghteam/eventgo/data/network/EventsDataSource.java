package com.ghteam.eventgo.data.network;

import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.util.LiveDataList;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by nikit on 10.12.2017.
 */

public class EventsDataSource {
    private FirebaseFirestore firestore;

    private LiveDataList<Event> eventsList;

    private static EventsDataSource sInstance;

    private EventsDataSource() {
        firestore = FirebaseFirestore.getInstance();
        eventsList = new LiveDataList<>();
    }

    public static EventsDataSource getInstance() {
        if (sInstance == null) {
            synchronized (EventsDataSource.class) {
                if (sInstance == null) {
                    sInstance = new EventsDataSource();
                }
            }
        }
        return sInstance;
    }


    public LiveDataList<Event> loadEvents() {

        firestore.collection("events").limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots.size() > 0) {
                    eventsList.setValue(documentSnapshots.toObjects(Event.class));
                }
            }
        });

        return eventsList;
    }


}


