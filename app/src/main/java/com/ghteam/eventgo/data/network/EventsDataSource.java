package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private LiveDataList<Event> downloadedEventsList;

    public static final String TAG = EventsDataSource.class.getSimpleName();

    private static EventsDataSource sInstance;

    private Event lastLoadedEvent;

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


    public void loadNextEvents(int count, final OnTaskStatusChangeListener listener) {
        listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.IN_PROGRESS);

        Query query = firestore.collection("events").orderBy("id", Query.Direction.DESCENDING);

        if (lastLoadedEvent != null) {
            query.startAfter(lastLoadedEvent.getId());
            Log.d(TAG, "loadNextEvents: " + lastLoadedEvent.getId());
        }

        query.limit(count).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots != null && documentSnapshots.size() > 0) {
                    listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.SUCCESS);
                    lastLoadedEvent = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1).toObject(Event.class);
                    downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onStatusChanged(OnTaskStatusChangeListener.TaskStatus.FAILED);
                Log.w(TAG, "onFailure: ", e);
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
//                        Log.d(TAG, "onEvent: " + documentSnapshots.toObjects(Event.class).toString());

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


