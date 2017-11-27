package com.ghteam.eventgo.ui.activity.login;

import android.app.Activity;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.databinding.ActivityLoginBinding;
import com.ghteam.eventgo.ui.activity.eventslist.EventsListActivity;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.ui.activity.singup.SignUpActivity;
import com.ghteam.eventgo.util.FacebookUserJsonConverter;
import com.ghteam.eventgo.util.LoginInResult;
import com.ghteam.eventgo.util.PrefsUtil;
import com.ghteam.eventgo.util.ProgressBarUtil;
import com.ghteam.eventgo.util.network.AccountStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends LifecycleActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CallbackManager mCallbackManager;

    private LoginViewModel viewModel;
    private ActivityLoginBinding activityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        ButterKnife.bind(this);

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }
        Log.d(TAG, "onCreate: userInfo " + firebaseAuth.getCurrentUser());

        viewModel = ViewModelProviders.of(this,
                new LoginViewModel.LoginViewModelFactory(Repository.getInstance(this)))
                .get(LoginViewModel.class);

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();

        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerViewModelObservers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeViewModelObservers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginWithEmail(String email, String password) {

        viewModel.getLoginInResult().setValue(LoginInResult.IN_PROCESS);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        viewModel.getLoginInResult().setValue(LoginInResult.SUCCESS);
                        PrefsUtil.setLoggedType(PrefsUtil.LOGGED_TYPE_EMAIL);
                        //TODO

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO
                viewModel.getLoginInResult().setValue(LoginInResult.ACCOUNT_NOT_FOUND_ERROR);
            }
        });
    }


    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
//        String[] fields = {"first_name", "last_name", "birthday", "about", "picture"};
        viewModel.getLoginInResult().setValue(LoginInResult.IN_PROCESS);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            
                            PrefsUtil.setLoggedType(PrefsUtil.LOGGED_TYPE_FACEBOOK);
                            viewModel.getLoginInResult().setValue(LoginInResult.SUCCESS);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            viewModel.getLoginInResult().setValue(LoginInResult.ERROR);
                        }
                    }
                });

    }

    private void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private void addNewFacebookUser() {
        Log.d(TAG, "addNewFacebookUser: ");
        viewModel.addNewFacebookUser(firebaseAuth.getCurrentUser().getUid(),
                AccessToken.getCurrentAccessToken(),
                new FirebaseDatabaseManager.OnPullUserResultListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                        startActivity(ProfileSettingsActivity.class);
                    }

                    @Override
                    public void onFail(Exception e) {
                        //TODO:
                    }
                });
    }

    private Observer<AccountStatus> accountStatusObserver = new Observer<AccountStatus>() {
        @Override
        public void onChanged(@Nullable AccountStatus accountStatus) {
            switch (accountStatus) {
                case IN_PROCESS:
                    ProgressBarUtil.showProgressBar(activityBinding.progressBar,
                            LoginActivity.this,
                            activityBinding.mainContainer);
                    return;

                case OK:
                    startActivity(EventsListActivity.class);
                    return;

                case REQUIRE_UPDATE_PROFILE:
                    Log.d(TAG, "onChanged: " + PrefsUtil.getLoggedType());
                    if (PrefsUtil.getLoggedType().equals(PrefsUtil.LOGGED_TYPE_FACEBOOK)) {
                        addNewFacebookUser();
                    } else {
                        startActivity(ProfileSettingsActivity.class);
                    }
                    return;

                default:
                    ProgressBarUtil.hideProgressBar(activityBinding.progressBar,
                            LoginActivity.this,
                            activityBinding.mainContainer);
                    return;
            }
        }
    };

    private Observer<LoginInResult> loginInResultObserver = new Observer<LoginInResult>() {
        @Override
        public void onChanged(@Nullable LoginInResult loginInResult) {
            switch (loginInResult) {
                case IN_PROCESS:
                    ProgressBarUtil.showProgressBar(activityBinding.progressBar,
                            LoginActivity.this,
                            activityBinding.mainContainer);
                    return;

                default:
                    ProgressBarUtil.hideProgressBar(activityBinding.progressBar,
                            LoginActivity.this,
                            activityBinding.mainContainer);
            }
        }
    };

    private void registerViewModelObservers() {
        viewModel.getAccountStatus().observeForever(accountStatusObserver);
        viewModel.getLoginInResult().observeForever(loginInResultObserver);
    }

    private void removeViewModelObservers() {
        viewModel.getAccountStatus().removeObserver(accountStatusObserver);
        viewModel.getLoginInResult().removeObserver(loginInResultObserver);
    }

    @OnClick(R.id.tv_create_account)
    void showCreateSingUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.bt_sign_in)
    void login() {
        loginWithEmail(activityBinding.etEmail.getText().toString(), activityBinding.etPassword.getText().toString());
    }

    @OnClick(R.id.bt_facebook_login)
    void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email", "user_birthday"));
    }

}
