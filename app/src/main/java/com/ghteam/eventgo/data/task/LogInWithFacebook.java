package com.ghteam.eventgo.data.task;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * Created by nikit on 20.01.2018.
 */

public class LogInWithFacebook extends BaseTask<Void, String> {

    private static final String TAG = LogInWithFacebook.class.getSimpleName();

    private Activity mActivity;
    private CallbackManager callbackManager;

    public LogInWithFacebook(Activity activity, CallbackManager callbackManager) {
        this.mActivity = activity;
        this.callbackManager = callbackManager;
    }

    @Override
    public void execute(Void... params) {

        changeStatus(TaskStatus.IN_PROGRESS);

        final LoginManager loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginManager);
                //TODO: handle the result;
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error);
                exception = error;
            }
        });


        LoginManager.getInstance().logInWithReadPermissions(mActivity,
                Arrays.asList("public_profile", "email", "user_birthday"));

    }

    private void handleFacebookAccessToken(final AccessToken token) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            PrefsUtil.setLoggedType(PrefsUtil.LOGGED_TYPE_FACEBOOK);
                            changeStatus(TaskStatus.SUCCESS);
                            publishResult(task.getResult().getUser().getUid());
                        } else {
                            if(task.getException()!=null){
                                Log.w(TAG, "onComplete: " , task.getException());
                                exception = task.getException();
                            }
                            changeStatus(TaskStatus.ERROR);
                        }
                    }
                });

    }
}
