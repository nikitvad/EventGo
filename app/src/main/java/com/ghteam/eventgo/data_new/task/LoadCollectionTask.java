package com.ghteam.eventgo.data_new.task;

import android.util.Log;

import com.ghteam.eventgo.data_new.NetworkTask;
import com.ghteam.eventgo.data_new.TaskResultListener;
import com.ghteam.eventgo.data_new.TaskStatus;
import com.ghteam.eventgo.data_new.TaskStatusListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 14.01.2018.
 */

public class LoadCollectionTask<R> implements NetworkTask<Integer> {

    private static final String TAG = LoadCollectionTask.class.getSimpleName();

    private CollectionReference collectionReference;

    private TaskStatusListener taskStatusListener;
    private TaskResultListener<List<R>> taskResultListener;

    private Exception exception;
    private Class resultType;

    public LoadCollectionTask(Class resultType, CollectionReference collectionReference) {
        this.resultType = resultType;
        this.collectionReference = collectionReference;
    }

    @Override
    public void execute(Integer... params) {
        changeTaskStatus(TaskStatus.IN_PROGRESS);
        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e == null) {
                    List<R> list = documentSnapshots.toObjects(resultType);
                    publishResult(list);
                    changeTaskStatus(TaskStatus.SUCCESS);
                } else {
                    exception = e;

                    Log.w(TAG, "onEvent: ", e);
                    changeTaskStatus(TaskStatus.FAILED);
                }
            }
        };

        if (params.length > 0) {
            collectionReference.limit(params[0]).addSnapshotListener(eventListener);
        } else {
            collectionReference.addSnapshotListener(eventListener);
        }

    }

    public Exception getException() {
        return exception;
    }

    public LoadCollectionTask<R> addTaskStatusListener(TaskStatusListener listener) {
        taskStatusListener = listener;
        return this;
    }

    public LoadCollectionTask<R> addTaskResultListener(TaskResultListener<List<R>> listener) {
        taskResultListener = listener;
        return this;
    }

    private void publishResult(List<R> result) {
        if (taskResultListener != null) {
            taskResultListener.onResult(result);
        }
    }

    private void changeTaskStatus(TaskStatus status) {
        if (taskStatusListener != null) {
            taskStatusListener.onStatusChanged(status);
        }
    }


}
