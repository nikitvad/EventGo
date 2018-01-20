package com.ghteam.eventgo.ui.activity.login;

import android.app.Activity;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
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
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.firebase.auth.FirebaseAuth;


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


    private void startActivity(Class<? extends Activity> activityClass) {
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
            }
        });

        activityBinding.btFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            viewModel.logInWithFacebook(LoginActivity.this, mCallbackManager);
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

