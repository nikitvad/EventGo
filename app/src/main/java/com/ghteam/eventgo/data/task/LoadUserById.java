package com.ghteam.eventgo.data.task;

import android.util.Log;

import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Created by nikit on 01.02.2018.
 */

public class LoadUserById extends BaseTask<String, User> {
    private static final String TAG = LoadUserById.class.getSimpleName();

    @Override
    public void execute(String... params) {
        changeStatus(TaskStatus.IN_PROGRESS);
        CollectionReference collectionReference = FirestoreUtil.getReferenceToUsers();

        for (String id : params) {
            collectionReference.document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e == null && documentSnapshot.exists()) {
                        publishResult(documentSnapshot.toObject(User.class));
                        changeStatus(TaskStatus.SUCCESS);
                    } else {
                        exception = e;
                        changeStatus(TaskStatus.ERROR);
                        Log.w(TAG, "onEvent: ", e);
                    }
                }
            });
        }


    }
}
