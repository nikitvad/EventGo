package com.ghteam.eventgo.data.task;

import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.network.LocationFilter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 17.01.2018.
 */

public class LoadEventsByLocation extends BaseTask<Integer, List<Event>> {

    private static final String TAG = LoadEventsByLocation.class.getSimpleName();


    private LocationFilter mLocationFilter;

    public LoadEventsByLocation(LocationFilter locationFilter) {
        mLocationFilter = locationFilter;
    }

    @Override
    public void execute(Integer... params) {
        changeStatus(TaskStatus.IN_PROGRESS);

        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    List<Event> events = documentSnapshots.toObjects(Event.class);
                    publishResult(events);
                    changeStatus(TaskStatus.SUCCESS);

                } else {
                    Log.w(TAG, "onEvent: ", e);
                    LoadEventsByLocation.this.exception = e;
                    changeStatus(TaskStatus.ERROR);
                }
            }
        };

    }
}
