package com.ghteam.eventgo.data.task;

import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


/**
 * Created by nikit on 22.01.2018.
 */

public class LoadEventsAfter extends BaseTask<String, List<Event>> {

    private static final String TAG = LoadEventsAfter.class.getSimpleName();

    private DocumentSnapshot lastLoadedDocument;

    private EventListener<QuerySnapshot> mEventListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e == null) {
                publishResult(documentSnapshots.toObjects(Event.class));

                if (documentSnapshots.size() > 0) {
                    lastLoadedDocument = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                }

                changeStatus(TaskStatus.SUCCESS);
            } else {
                exception = e;
                Log.w(TAG, "onEvent: ", e);
                changeStatus(TaskStatus.ERROR);
            }
        }
    };

    private Query mQuery = FirestoreUtil.getReferenceToEvents();

    @Override
    public void execute(String... params) {
        changeStatus(TaskStatus.IN_PROGRESS);

        String eventId = params[0];
        int limit = Integer.parseInt(params[1]);

        if (lastLoadedDocument != null) {
            mQuery.orderBy("date").limit(limit).startAfter(lastLoadedDocument).addSnapshotListener(mEventListener);
        } else {
            mQuery.orderBy("date").limit(limit).addSnapshotListener(mEventListener);

        }

    }
}
