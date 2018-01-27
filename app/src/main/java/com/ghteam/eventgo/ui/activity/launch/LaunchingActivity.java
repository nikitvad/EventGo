package com.ghteam.eventgo.ui.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.ActivityLaunchingBinding;
import com.ghteam.eventgo.ui.activity.createevent.CreateEventActivity;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsActivity;
import com.ghteam.eventgo.ui.activity.eventslist.EventsActivity;
import com.ghteam.eventgo.ui.activity.login.LoginActivity;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.ui.activity.userslist.PeopleActivity;
import com.ghteam.eventgo.util.PrefsUtil;
import com.google.firebase.auth.FirebaseAuth;

public class LaunchingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityLaunchingBinding activityBinding;

    public static final String TAG = LaunchingActivity.class.getSimpleName();


    public Class activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_launching);


        if (PrefsUtil.getLoggedType() == PrefsUtil.LOGGED_TYPE_NONE) {
            activity = LoginActivity.class;
        } else {
            activity = EventsActivity.class;
        }

//        repository.loadEvents(20);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(LaunchingActivity.this, activity);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        }, 5000);

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

        activityBinding.btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateEventActivity.class);
            }
        });

        activityBinding.btPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PeopleActivity.class);
            }
        });

        activityBinding.btEventsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EventsActivity.class);
            }
        });

        activityBinding.btEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EventDetailsActivity.class);
            }
        });

        activityBinding.btPushDemoEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new PushDemoEvents().push();
            }
        });

        activityBinding.btPushDemoUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new PushDemoUsersTest().pushUsers();
            }
        });

    }


    private void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(LaunchingActivity.this, activity);
        startActivity(intent);
    }
}
