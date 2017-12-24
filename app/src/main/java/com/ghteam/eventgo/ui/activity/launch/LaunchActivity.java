package com.ghteam.eventgo.ui.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.ActivityLaunchBinding;
import com.ghteam.eventgo.ui.activity.createevent.CreateEventActivity;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsActivity;
import com.ghteam.eventgo.ui.activity.eventslist.EventsListActivity;
import com.ghteam.eventgo.ui.activity.login.LoginActivity;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;
import com.ghteam.eventgo.ui.activity.userslist.PeopleActivity;
import com.ghteam.eventgo.util.network.LocationUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
                startActivity(EventsListActivity.class);
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

        Log.d("Distance", "onCreate: " + LocationUtil.calculateDistance(49.415398, 32.030653
                , 48.881528, 30.390074));
    }


    private void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(LaunchActivity.this, activity);
        startActivity(intent);
    }

    private void bindEventTime() {
        SimpleDateFormat weekDay = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.ENGLISH);

        SimpleDateFormat numb = new SimpleDateFormat("dd", Locale.ENGLISH);


        Log.d(TAG, "bindEventTime: " + weekDay.format(new Date()));
        Log.d(TAG, "bindEventTime: " + month.format(new Date()));
        Log.d(TAG, "bindEventTime: " + numb.format(new Date()));
    }
}
