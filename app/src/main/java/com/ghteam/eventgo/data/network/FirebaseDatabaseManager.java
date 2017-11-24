package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseDatabaseManager {

    public static final String REF_USERS = "users";


    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void pullUserInfo(String uid, User user, @Nullable final OnPullUserResultListener listener) {

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


    public static void fetchUserById(String uid) {
        firestore.document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (documentSnapshot != null) {

                }
            }
        });
    }


    public interface OnFetchUserResultListener {
        void onSuccess(User user);

        void onFailed();
    }

    public interface OnPullUserResultListener {
        void onSuccess();

        void onFail(Exception e);
    }


}
