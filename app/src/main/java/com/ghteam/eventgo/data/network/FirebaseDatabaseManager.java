package com.ghteam.eventgo.data.network;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.entity.UserEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by nikit on 19.11.2017.
 */

class FirebaseDatabaseManager {

    public static final String REF_USERS = "users";


    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void pullNewUser(String uid, UserEntry user, final OnPullResultListener listener) {

        firestore.collection(REF_USERS).document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) {
                            listener.onFail(e);
                        }
                    }
                });
    }


    public interface OnPullResultListener {
        void onSuccess();

        void onFail(Exception e);
    }
}
