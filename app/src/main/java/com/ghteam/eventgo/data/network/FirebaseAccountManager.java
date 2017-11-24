package com.ghteam.eventgo.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data.entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseAccountManager {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static MutableLiveData<User> appUser = new MutableLiveData<>();
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final String FIRESTORE_USERS = "users";

    private static final String TAG = FirebaseAccountManager.class.getSimpleName();

    public static void createNewAccount(String email, String password, final User user,
                                        @Nullable final OnResultListener listener) {

        Log.d(TAG, "createNewAccount: ");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabaseManager.pullUserInfo(authResult.getUser().getUid(), user,
                                new FirebaseDatabaseManager.OnPullUserResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "onSuccess: userID:");

                                        if (listener != null) {
                                            listener.onSuccess();
                                        }
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Log.d(TAG, "onFail: " + e.getMessage());
                                        if (listener != null) {
                                            listener.onFailed();
                                        }
                                    }
                                });
                    }
                });
    }


    public static MutableLiveData<User> getCurrentUser() {

        Log.d(TAG, "getCurrentUser: ");
        if (mAuth.getCurrentUser() != null) {
            firestore.collection(FIRESTORE_USERS).document(mAuth.getCurrentUser().getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "onEvent: " + documentSnapshot.toString());
                                appUser.setValue(documentSnapshot.toObject(User.class));
                            }
                        }
                    });
        }


        return appUser;
    }

    public interface OnResultListener {
        void onSuccess();

        void onFailed();
    }

}
