package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseDatabaseManager {

    public static final String REF_USERS = "users";

    public static final String REF_EVENTS = "events";


    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void pushUserInfo(String uid, User user, @Nullable final OnPullUserResultListener listener) {

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

    public static void loadUsers(final OnLoadUsersCompleteListener listener) {

        firestore.collection(REF_USERS).limit(30).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots.size() > 0) {
                    listener.onComplete(documentSnapshots.toObjects(User.class));
                } else {
                    listener.onComplete(null);
                }
            }
        });
    }

    public static void pushNewEvent(Event event, OnSuccessListener<DocumentReference> onSuccessListener,
                                    OnFailureListener onFailureListener) {
        firestore.collection(REF_EVENTS).add(event)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public interface OnLoadUsersCompleteListener {
        void onComplete(@Nullable List<User> users);
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
