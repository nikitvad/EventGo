package com.ghteam.eventgo.data_new.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ghteam.eventgo.data_new.entity.Event;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.LocationUtil;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener.TaskStatus;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 10.12.2017.
 */

public class EventsDataSource {
//    private FirebaseFirestore firestore;

    private CollectionReference collectionEvents = FirebaseFirestore.getInstance().collection("events");
    private LiveDataList<Event> downloadedEventsList;


    public static final String TAG = EventsDataSource.class.getSimpleName();

    private static EventsDataSource sInstance;

    private Event lastLoadedEvent;

    private EventsDataSource() {
//        firestore = FirebaseFirestore.getInstance();
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

    public void searchEvents(LocationFilter searchFilter, final OnTaskStatusChangeListener listener) {
        listener.onStatusChanged(TaskStatus.IN_PROGRESS);

        LatLng latLng = new LatLng(searchFilter.getLocation().latitude,
                searchFilter.getLocation().longitude);

        final LatLng[] rectangle = LocationUtil.calculateRectangle(latLng, searchFilter.getHeight(),
                searchFilter.getWidth());

        Query query = collectionEvents.whereGreaterThan("location.longitude", rectangle[0].longitude)
                .whereLessThan("location.longitude", rectangle[1].longitude);
//                .whereLessThan("location.latitude", rectangle[0].latitude)
//                .whereGreaterThan("location.latitude", rectangle[1].latitude);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {

                    listener.onStatusChanged(TaskStatus.SUCCESS);

                    Log.d(TAG, "onEvent: ");
                    if (documentSnapshots.size() > 0) {

                        List<Event> eventList = documentSnapshots.toObjects(Event.class);


                        for (int i = 0; i < eventList.size(); i++) {
                            if (eventList.get(i).getLocation().getLatitude() > rectangle[0].latitude
                                    || eventList.get(i).getLocation().getLatitude() < rectangle[1].latitude) {
                                eventList.remove(eventList.get(i));
                            }
                        }

                        downloadedEventsList.setValue(eventList);

                    } else {
                        Log.w(TAG, "onEvent: ", e);
                        listener.onStatusChanged(TaskStatus.FAILED);
                    }
                }
            }
        });
    }

    public void loadNextEvents(int count, final OnTaskStatusChangeListener listener) {
        listener.onStatusChanged(TaskStatus.IN_PROGRESS);

        Query query = collectionEvents.orderBy("id", Query.Direction.DESCENDING);

        if (lastLoadedEvent != null) {
            query.startAfter(lastLoadedEvent.getId());
            Log.d(TAG, "loadNextEvents: " + lastLoadedEvent.getId());
        }

        query.limit(count).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots != null && documentSnapshots.size() > 0) {
                    listener.onStatusChanged(TaskStatus.SUCCESS);
                    lastLoadedEvent = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1).toObject(Event.class);
                    downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onStatusChanged(TaskStatus.FAILED);
                Log.w(TAG, "onFailure: ", e);
            }
        });

    }

    public void loadEvents(final OnTaskStatusChangeListener listener) {

        listener.onStatusChanged(TaskStatus.IN_PROGRESS);

        collectionEvents.limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e == null) {
                    if (documentSnapshots != null && documentSnapshots.size() > 0) {
                        downloadedEventsList.setValue(documentSnapshots.toObjects(Event.class));

                    }
                    listener.onStatusChanged(TaskStatus.SUCCESS);

                } else {
                    Log.w(TAG, "onEvent: " + e.getMessage());
                    listener.onStatusChanged(TaskStatus.FAILED);
                }
            }
        });
    }


}


