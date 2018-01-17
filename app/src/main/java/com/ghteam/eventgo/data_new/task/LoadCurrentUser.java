package com.ghteam.eventgo.data_new.task;

import android.util.Log;

import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Created by nikit on 17.01.2018.
 */

public class LoadCurrentUser extends BaseNetworkTask<Void, User> {

    private static final String TAG = LoadCurrentUser.class.getSimpleName();

    @Override
    public void execute(Void... params) {
        changeStatus(TaskStatus.IN_PROGRESS);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference usersCollection = FirestoreUtil.getReferenceToUsers();

        usersCollection.document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e==null){
                    publishResult(documentSnapshot.toObject(User.class));
                    changeStatus(TaskStatus.SUCCESS);

                }else{
                    exception = e;
                    changeStatus(TaskStatus.FAILED);
                    Log.w(TAG, "onEvent: ", e );
                }
            }
        });

    }
}
