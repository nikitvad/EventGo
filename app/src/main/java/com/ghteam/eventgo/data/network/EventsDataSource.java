package com.ghteam.eventgo.data.network;

import android.util.Log;

import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by nikit on 10.12.2017.
 */

public class EventsDataSource {
    private FirebaseFirestore firestore;

    private DocumentSnapshot lastLoadedDocument;
    private boolean isCollectionFullyLoaded = false;

    private LiveDataList<Event> downloadedEventsList;

    public static final String TAG = EventsDataSource.class.getSimpleName();

    private static EventsDataSource sInstance;

    private EventsDataSource() {
        firestore = FirebaseFirestore.getInstance();
        downloadedEventsList = new LiveDataList<>();
        lastLoadedDocument = null;
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

    public void loadNextEvents(int count, final OnTaskStatusChangeListener listener) {
        listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS);


        Query query = firestore.collection("events");

        query.orderBy("name");

        if (lastLoadedDocument != null) {
            Log.d(TAG, "loadEvents: " + lastLoadedDocument.getId());
            query.startAfter(lastLoadedDocument);

        }

        query.limit(10).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);

                    lastLoadedDocument = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    Log.d(TAG, "onEvent: " + documentSnapshots.getDocuments().get(documentSnapshots.size() - 1).getId());
                    downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));

                    Log.d(TAG, "onEvent: " + documentSnapshots.size());

                } else {
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.FAILED);
                    Log.w(TAG, "onEvent: ", e);
                }
            }
        });


    }

    public void loadEvents(final OnTaskStatusChangeListener listener) {

        listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS);

        firestore.collection("events").limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e == null) {
                    if (documentSnapshots != null && documentSnapshots.size() > 0) {
                        downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));
                    }
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);

                } else {
                    Log.w(TAG, "onEvent: " + e.getMessage());
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.FAILED);
                }
            }
        });


    }


}


