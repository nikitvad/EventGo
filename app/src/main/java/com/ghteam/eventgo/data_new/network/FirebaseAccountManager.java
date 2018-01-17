package com.ghteam.eventgo.data_new.network;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.util.network.AccountStatus;
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
    private static MutableLiveData<AccountStatus> currentAccountStatus = new MutableLiveData<>();
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final String FIRESTORE_USERS = "users";

    private static final String TAG = FirebaseAccountManager.class.getSimpleName();

    static {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged: ");
                if (mAuth.getCurrentUser() != null) {
                    currentAccountStatus.setValue(AccountStatus.IN_PROCESS);
                    getCurrentAccountStatus();
                } else {
                    currentAccountStatus.setValue(AccountStatus.NONE);
                }
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
                        FirebaseDatabaseManager.pushUserInfo(authResult.getUser().getUid(), user,
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
                        appUser.setValue(documentSnapshot.toObject(User.class));
                        Log.d(TAG, "onEvent: " + documentSnapshot.toObject(User.class).toString());

                    } else {
                        //There is no user in DB whit that id so we initialize new Instance of user to add it to db
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

    public static MutableLiveData<AccountStatus> getCurrentAccountStatus() {

        if (mAuth.getCurrentUser() != null) {
            loadUserByID(mAuth.getCurrentUser().getUid(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (e == null) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);

                            if (user.getFirstName().length() > 0 && user.getInterests().size() > 0) {
                                currentAccountStatus.setValue(AccountStatus.OK);
                            } else {
                                currentAccountStatus.setValue(AccountStatus.REQUIRE_UPDATE_PROFILE);
                            }
                        } else {
                            currentAccountStatus.setValue(AccountStatus.REQUIRE_UPDATE_PROFILE);
                        }
                    } else {
                        currentAccountStatus.setValue(AccountStatus.NONE);
                    }
                }
            });
        }

        return currentAccountStatus;
    }

    public interface OnResultListener {
        void onSuccess();

        void onFailed();
    }

}
