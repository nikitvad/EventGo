package com.ghteam.eventgo.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
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
    private static MutableLiveData<Boolean> isRequireProfileUpdate = new MutableLiveData<>();
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final String FIRESTORE_USERS = "users";

    private static final String TAG = FirebaseAccountManager.class.getSimpleName();

    static {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged: ");
                getIsRequireProfileUpdate();
            }
        });

    }

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

        if (mAuth.getCurrentUser() != null) {
            loadUserByID(mAuth.getCurrentUser().getUid(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onEvent: " + documentSnapshot.toString());
                        appUser.setValue(documentSnapshot.toObject(User.class));
                    }else{
                        appUser.setValue(new User());
                    }
                }
            });
        }

        return appUser;
    }


    public static void loadUserByID(String id, EventListener<DocumentSnapshot> eventListener) {
        firestore.collection(FIRESTORE_USERS).document(id)
                .addSnapshotListener(eventListener);
    }

    public static MutableLiveData<Boolean> getIsRequireProfileUpdate() {

        if (mAuth.getCurrentUser() != null) {
            loadUserByID(mAuth.getCurrentUser().getUid(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        if (user.getFirstName().length() > 0 && user.getInterests().size() > 0) {
                            isRequireProfileUpdate.setValue(false);
                        } else {
                            isRequireProfileUpdate.setValue(true);
                        }
                    } else {
                        isRequireProfileUpdate.setValue(true);
                    }
                }
            });
        }

        return isRequireProfileUpdate;
    }

    public interface OnResultListener {
        void onSuccess();

        void onFailed();
    }

}
