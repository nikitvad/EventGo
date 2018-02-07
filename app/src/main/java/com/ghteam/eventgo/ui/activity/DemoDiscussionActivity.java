package com.ghteam.eventgo.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.network.EventDiscussionClient;
import com.ghteam.eventgo.databinding.ActivityDemoDiscussionBinding;
import com.ghteam.eventgo.ui.fragment.eventdiscussion.EventDiscussionFragment;

import java.util.Date;

public class DemoDiscussionActivity extends AppCompatActivity {

    ActivityDemoDiscussionBinding activityBinding;

    EventDiscussionClient discussionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo_discussion);


        EventDiscussionFragment fragment = EventDiscussionFragment.newInstance("1DaM4uttOQV6EHYPwnMu");

        discussionClient = new EventDiscussionClient("1DaM4uttOQV6EHYPwnMu");


        getSupportFragmentManager().beginTransaction().add(R.id.discussion_container, fragment).commit();

        activityBinding.btGenerateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscussionMessage discussionMessage = new DiscussionMessage();
                discussionMessage.setDate(new Date());
                discussionMessage.setMessage("sdfsdgsdfg sdfg sdfg sdfg sdg asdgwsdg asfasdg waf sad gfasd fvsdg asd fsdg asdgv qweg SADB ASXVB SDG");
                discussionMessage.setOwnerName("Jon Snow");
                discussionMessage.setOwnerProfileImage(getResources().getString(R.string.def_profile_picture));

                discussionClient.sendMessage(discussionMessage);

            }
        });
    }
}
