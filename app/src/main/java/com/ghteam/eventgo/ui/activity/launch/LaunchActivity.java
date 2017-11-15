package com.ghteam.eventgo.ui.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.ui.activity.login.LoginActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        startActivity(LoginActivity.class);
    }




    private void startActivity(Class<? extends Activity> activity){

        Intent intent = new Intent(LaunchActivity.this, activity);
        startActivity(intent);

    }
}
