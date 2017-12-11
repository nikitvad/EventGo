package com.ghteam.eventgo.data.network;

import android.util.Log;

import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by nikit on 10.12.2017.
 */

public class EventsDataSource {
    private FirebaseFirestore firestore;

    private LiveDataList<Event> downloadedEventsList;

    public static final String TAG = EventsDataSource.class.getSimpleName();

    private static EventsDataSource sInstance;

    private EventsDataSource() {
        firestore = FirebaseFirestore.getInstance();
        downloadedEventsList = new LiveDataList<>();
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


    public LiveDataList<Event> getCurrentEvents() {
        return downloadedEventsList;
    }

    public void loadEvents(final OnTaskStatusChangeListener listener) {

        listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS);

        firestore.collection("events").limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (documentSnapshots != null && documentSnapshots.size() > 0) {
                    downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);
                } else if (e != null) {
                    Log.w(TAG, "onEvent: " + e.getMessage());
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.FAILED);
                } else {
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);
                }
            }
        });


    }


}


