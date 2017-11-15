package com.ghteam.eventgo.ui.activity.profilesettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.ui.activity.eventslist.EventsListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileSettingsActivity extends AppCompatActivity {

    @BindView(R.id.iv_profile_photo)
    ImageView ivProfilePhoto;

    @BindView(R.id.et_describe_yourself)
    EditText etDescribeYourself;

    @BindView(R.id.cb_public_account)
    CheckBox cbPublicProfile;

    @BindView(R.id.tv_what_is_public_profile)
    TextView tvWhatIsPublicProfile;

    @BindView(R.id.rl_category_container)
    RelativeLayout rlCategoryContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_submit)
    void submit() {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }
}
