package com.ghteam.eventgo.ui.activity.singup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.ui.activity.profilesettings.ProfileSettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingUpActivity extends AppCompatActivity {

    @BindView(R.id.iv_profile_photo)
    ImageView ivProfilePhoto;

    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @BindView(R.id.et_last_name)
    EditText etLastName;

    @BindView(R.id.tv_birthday)
    TextView tvBirthday;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @OnClick(R.id.bt_submit)
    void signUp() {
        Intent intent = new Intent(this, ProfileSettingsActivity.class);
        startActivity(intent);
    }
}
