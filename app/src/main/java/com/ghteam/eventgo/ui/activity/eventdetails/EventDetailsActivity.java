package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.Location;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.databinding.ActivityEventDetailsBinding;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.util.InjectorUtil;

public class EventDetailsActivity extends AppCompatActivity {

    private ActivityEventDetailsBinding activityBinding;
    private RecyclerView rvImagePreviews;
    private ImagePreviewsRecyclerAdapter mImagePreviewsAdapter;

    private EventDetailsViewModel viewModel;

    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);


        String eventId = getIntent().getStringExtra("eventId");

        EventDetailsViewModelFactory viewModelFactory = InjectorUtil
                .provideEventDetailsViewModelFactory(this, eventId);


        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(EventDetailsViewModel.class);

        rvImagePreviews = activityBinding.rvImagePreviews;
        rvImagePreviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImagePreviews.setAdapter(mImagePreviewsAdapter);

        registerViewModelObservers();
    }


    private void registerViewModelObservers() {
        viewModel.getEvent().observeForever(new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
                activityBinding.setVariable(BR.event, event);
            }
        });

        viewModel.getCategory().observeForever(new Observer<Category>() {
            @Override
            public void onChanged(@Nullable Category category) {
                activityBinding.setVariable(BR.category, category);
            }
        });

        viewModel.getLocation().observeForever(new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                activityBinding.setVariable(BR.location, location);
            }
        });

        viewModel.getUser().observeForever(new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                activityBinding.setVariable(BR.user, user);
            }
        });
    }

}
