package com.ghteam.eventgo.data_new.task;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

/**
 * Created by nikit on 17.01.2018.
 */

public class UpdateUser extends BaseNetworkTask<Void, Void> {

    private User mUser;
    private String mUid;

    private static final String TAG = UpdateUser.class.getSimpleName();

    public UpdateUser(User user, String uid) {
        mUid = uid;
        mUser = user;
    }

    @Override
    public void execute(Void... params) {
        changeStatus(TaskStatus.IN_PROGRESS);
        CollectionReference usersCollection = FirestoreUtil.getReferenceToUsers();

        mUser.setId(mUid);

        usersCollection.document(mUid).set(mUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        changeStatus(TaskStatus.SUCCESS);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        changeStatus(TaskStatus.FAILED);
                        UpdateUser.this.exception = e;
                        Log.w(TAG, "onFailure: ", e);
                    }
                });
    }
}
