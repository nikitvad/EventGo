package com.ghteam.eventgo.data.task;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 08.01.2018.
 */

public class FirestoreCollectionLoader<R> implements NetworkTask<Integer>, TaskStatusInterface {

    protected TaskResultListener<List<R>> taskResultListener;
    protected TaskStatusListener taskStatusListener;

    protected Exception exception;

    protected TaskStatus taskStatus = TaskStatus.NONE;

    private CollectionReference collectionReference;
    private Class typeOfResult;

    private static final String TAG = FirestoreCollectionLoader.class.getSimpleName();


    public FirestoreCollectionLoader(CollectionReference collectionReference, Class<R> typeOfResult) {
        this.collectionReference = collectionReference;
        this.typeOfResult = typeOfResult;
    }

    protected void publishResult(List<R> result) {
        if (taskResultListener != null) {
            taskResultListener.onResult(result);
        }
    }

    public FirestoreCollectionLoader<R> addTaskStatusListener(TaskStatusListener listener) {
        taskStatusListener = listener;
        return this;
    }

    public FirestoreCollectionLoader<R> addTaskResultListener(TaskResultListener<List<R>> listener) {
        taskResultListener = listener;
        return this;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

    protected void changeStatus(TaskStatus status) {
        if (taskStatusListener != null) {
            taskStatusListener.onStatusChanged(status);
        }
        taskStatus = status;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    private EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e == null) {
                List<R> result = documentSnapshots.toObjects(typeOfResult);
                publishResult(result);
                changeStatus(TaskStatus.SUCCESS);

            } else {
                changeStatus(TaskStatus.ERROR);
                Log.w(TAG, "onEvent: ", e);
            }
        }
    };

    @Override
    public void execute(Integer... limit) {
        changeStatus(TaskStatus.IN_PROGRESS);

        if (limit.length > 0) {
            collectionReference.limit(limit[0]).addSnapshotListener(eventListener);
        } else {
            collectionReference.addSnapshotListener(eventListener);
        }

    }

}
