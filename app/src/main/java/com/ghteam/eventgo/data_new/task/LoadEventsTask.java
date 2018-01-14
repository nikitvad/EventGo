package com.ghteam.eventgo.data_new.task;

import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data_new.NetworkTask;
import com.ghteam.eventgo.data_new.TaskResultListener;
import com.ghteam.eventgo.data_new.TaskStatus;
import com.ghteam.eventgo.data_new.TaskStatusListener;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 04.01.2018.
 */

public class LoadEventsTask implements NetworkTask<Integer> {

    private CollectionReference referenceEvents = FirestoreUtil.getReferenceToEvents();

    private TaskStatusListener mTaskStatusListener;
    private TaskResultListener<List<Event>> mTaskResultListener;

    private Exception exception;

    @Override
    public void execute(Integer... params) {

        if (params.length > 0) {
            referenceEvents.limit(params[0]);
        }

        changeTaskStatus(TaskStatus.IN_PROGRESS);
        referenceEvents.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    List<Event> events = documentSnapshots.toObjects(Event.class);

                    publishResult(events);
                    changeTaskStatus(TaskStatus.SUCCESS);

                } else {
                    exception = e;
                    changeTaskStatus(TaskStatus.FAILED);
                }
            }
        });
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

    public LoadEventsTask setTaskStatusListener(TaskStatusListener listener) {
        mTaskStatusListener = listener;
        return this;
    }

    public LoadEventsTask setTaskResultListener(TaskResultListener<List<Event>> listener) {
        mTaskResultListener = listener;
        return this;
    }

    private void publishResult(List<Event> result) {
        if (mTaskResultListener != null) {
            mTaskResultListener.onResult(result);
        }
    }

    private void changeTaskStatus(TaskStatus status) {
        if (mTaskStatusListener != null) {
            mTaskStatusListener.onStatusChanged(status);
        }
    }

}
