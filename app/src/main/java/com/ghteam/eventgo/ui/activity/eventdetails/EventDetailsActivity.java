package com.ghteam.eventgo.ui.activity.eventdetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.database.Database;
import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.databinding.ActivityEventDetailsBinding;
import com.ghteam.eventgo.util.network.PushDemoEvents;
import com.ghteam.eventgo.util.network.PushUsersForTest;

import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    private ActivityEventDetailsBinding activityBinding;
    private RecyclerView rvImagePreviews;
    private ImagePreviewsRecyclerAdapter mImagePreviewsAdapter;

    private Database database;
    private Event event;

    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);

        event = new PushDemoEvents().generateDemoEvents().get(0);
        User user = new PushUsersForTest().generateUsers().get(2);
        activityBinding.setVariable(BR.user, user);
        activityBinding.setVariable(BR.event, event);

        mImagePreviewsAdapter = new ImagePreviewsRecyclerAdapter(event.getImages());
        mImagePreviewsAdapter.setOnItemClickListener(new ImagePreviewsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                mImagePreviewsAdapter.selectItem(pos);
            }
        });


        rvImagePreviews = activityBinding.rvImagePreviews;

        rvImagePreviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImagePreviews.setAdapter(mImagePreviewsAdapter);


        database = Database.getInstance(this);

//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                database.eventDao().insertAll(event);
//            }
//        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Event> eventList = database.eventDao().getAll();
                Log.d(TAG, "run: " + eventList);
            }
        });

    }
}
