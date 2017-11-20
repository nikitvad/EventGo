package com.ghteam.eventgo.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.entity.UserEntry;
import com.ghteam.eventgo.ui.activity.singup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by nikit on 19.11.2017.
 */

public class FirebaseAccountManager {

    private FirebaseAuth mAuth;
    private final MutableLiveData<UserEntry> appUser = new MutableLiveData<>();

    private static final String TAG = FirebaseAccountManager.class.getSimpleName();

    private static FirebaseAccountManager sInstance = null;

    private FirebaseAccountManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAccountManager getInstance() {
        if (sInstance == null) {
            synchronized (FirebaseAccountManager.class) {
                if (sInstance == null) {
                    sInstance = new FirebaseAccountManager();
                }
            }
        }
        return sInstance;
    }


    public void createNewAccount(String email, String password, final UserEntry userEntry) {
        Log.d(TAG, "createNewAccount: ");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabaseManager.pullNewUser(authResult.getUser().getUid(), userEntry,
                                new FirebaseDatabaseManager.OnPullResultListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "onSuccess: userID:");
                                    }

                                    @Override
                                    public void onFail(Exception e) {
                                        Log.d(TAG, "onFail: " + e.getMessage());
                                    }
                                });
                    }
                });
    }
}
