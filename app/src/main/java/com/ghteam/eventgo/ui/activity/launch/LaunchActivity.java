package com.ghteam.eventgo.ui.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.ActivityLaunchBinding;
import com.ghteam.eventgo.ui.activity.login.LoginActivity;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityLaunchBinding activityBinding;

    public static final String TAG = LaunchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_launch);

        activityBinding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });

        activityBinding.btProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ProfileSettingsActivity.class);
            }
        });
        if (mAuth.getCurrentUser() != null) {
            activityBinding.tvUserId.setText(mAuth.getCurrentUser().getUid());
        }

    }

    private void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(LaunchActivity.this, activity);
        startActivity(intent);
    }
}
