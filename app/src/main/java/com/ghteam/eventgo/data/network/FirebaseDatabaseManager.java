package com.ghteam.eventgo.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseDatabaseManager {

    public static final String REF_USERS = "users";

    public static final String REF_EVENTS = "events";

    public static final String TAG = FirebaseDatabaseManager.class.getSimpleName();

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void pushUserInfo(String uid, User user, @Nullable final OnPullUserResultListener listener) {

        user.setId(uid);
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

    public static void pushNewEvent(Event event, OnSuccessListener<Void> onSuccessListener,
                                    OnFailureListener onFailureListener) {

        DocumentReference documentReference = firestore.collection(REF_EVENTS).document();

        String eventId = documentReference.getId();

        Log.d(TAG, "pushNewEvent: " + eventId);

        event.setId(eventId);
        event.getCategory().setOwnerId(eventId);
        Log.d(TAG, "pushNewEvent: " + event.getDate());
        documentReference.set(event).addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

    }

    public interface OnLoadUsersCompleteListener {
        void onComplete(@Nullable List<User> users);
    }

    public interface OnPullUserResultListener {
        void onSuccess();

        void onFail(Exception e);
    }
}
