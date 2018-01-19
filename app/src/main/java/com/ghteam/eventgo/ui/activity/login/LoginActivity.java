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
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.data_new.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.databinding.ActivityLoginBinding;
import com.ghteam.eventgo.ui.activity.eventslist.EventsActivity;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.ui.activity.singup.SignUpActivity;
import com.ghteam.eventgo.util.AccountUtil;
import com.ghteam.eventgo.util.InjectorUtil;
import com.ghteam.eventgo.util.LoginInResult;
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


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

        bindClickListeners();

        LoginViewModel.LoginViewModelFactory viewModelFactory = InjectorUtil.provideLoginViewModelFactory(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

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

        registerViewModelObservers();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
            PrefsUtil.setLoggedType(PrefsUtil.LOGGED_TYPE_NONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.move_to_up_in, R.anim.move_to_up_out);
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
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

    private Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable User user) {
            switch (AccountUtil.checkUserAccount(user)) {
                case OK:
                    startActivity(EventsActivity.class);
                    return;
                case REQUIRE_UPDATE_PROFILE:
                    startActivity(ProfileSettingsActivity.class);
                    return;
            }
        }
    };

    private Observer<TaskStatus> logInTaskStatusObserver = new Observer<TaskStatus>() {
        @Override
        public void onChanged(@Nullable TaskStatus taskStatus) {
            switch (taskStatus) {
                case IN_PROGRESS:
                    showProgressBar();
                    return;
                default:
                    hideProgressBar();
                    return;
            }
        }
    };

    private void registerViewModelObservers() {
        viewModel.getCurrentUser().observe(this, userObserver);
        viewModel.getLogInTaskStatus().observe(this, logInTaskStatusObserver);
    }

    private static int startActivityCallCount = 0;

    private void startActivity(Class<? extends Activity> activityClass) {
        startActivityCallCount++;
        Log.d(TAG, "startActivity: " + startActivityCallCount);
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }

    private void bindClickListeners() {
        activityBinding.tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        activityBinding.btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.logInWithEmailAndPassword(activityBinding.etEmail.getText().toString(),
                        activityBinding.etPassword.getText().toString());
//                loginWithEmail(activityBinding.etEmail.getText().toString(), activityBinding.etPassword.getText().toString());
            }
        });

        activityBinding.btFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "email", "user_birthday"));
            }
        });
    }

    private void showProgressBar() {
        activityBinding.progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.linearLayout.setAlpha(0.5f);
    }

    private void hideProgressBar() {
        activityBinding.progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activityBinding.linearLayout.setAlpha(1f);
    }
}

