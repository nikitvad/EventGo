package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.databinding.ActivityEventDetailsBinding;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.ui.fragment.eventdiscussion.EventDiscussionFragment;
import com.ghteam.eventgo.util.ImageSwitcherPicasso;
import com.ghteam.eventgo.util.InjectorUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.view.View.GONE;

public class EventDetailsActivity extends AppCompatActivity {

    private ActivityEventDetailsBinding activityBinding;

    private EventDetailsViewModel viewModel;

    public static final String TAG = EventDetailsActivity.class.getSimpleName();

    private ImageSwitcherPicasso switcherPicasso;

    private ObservableInt currentImagePos;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);

        Event event1 = new Event();

        activityBinding.setVariable(BR.event, event1);

        setSupportActionBar(activityBinding.includeToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityBinding.includeToolbar.toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        currentImagePos = new ObservableInt(0);

        final String eventId = getIntent().getStringExtra("eventId");

        EventDetailsViewModelFactory viewModelFactory = InjectorUtil
                .provideEventDetailsViewModelFactory(this, "1DaM4uttOQV6EHYPwnMu");

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(EventDetailsViewModel.class);

        registerViewModelObservers();

        EventDiscussionFragment eventDiscussionFragment = EventDiscussionFragment.newInstance("1DaM4uttOQV6EHYPwnMu");
        getSupportFragmentManager().beginTransaction().replace(R.id.discussion_container,
                eventDiscussionFragment).commit();

        viewModel.getOwner().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                activityBinding.setVariable(BR.user, user);
            }
        });

        event = viewModel.getEvent();

        Log.d(TAG, "onCreate: " + event.toString());

        activityBinding.setVariable(BR.event, event);

        setUpImageSwitcher();

        bindEventTime(event.getDate());

        switcherPicasso = new ImageSwitcherPicasso(EventDetailsActivity.this, activityBinding.isImages);

        if (event.getRealmImages().size() > 0) {
            activityBinding.lfImages.setVisibility(View.VISIBLE);

            Picasso.with(this).load(event.getRealmImages().get(currentImagePos.get())).into(switcherPicasso);
            if (event.getRealmImages().size() == 1) {
                activityBinding.ivNextImage.setVisibility(GONE);
                activityBinding.ivPreviousImage.setVisibility(GONE);
            }

            currentImagePos.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    if (i >= event.getRealmImages().size()) {
                        activityBinding.ivNextImage.setVisibility(GONE);
                    } else {
                        activityBinding.ivNextImage.setVisibility(View.VISIBLE);
                    }

                    if (i == 0) {
                        activityBinding.ivPreviousImage.setVisibility(GONE);
                    } else {
                        activityBinding.ivPreviousImage.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

        bindClickListeners();
    }

    private void bindClickListeners() {

        activityBinding.ivPreviousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImagePos.get() - 1 >= 0) {
                    currentImagePos.set(currentImagePos.get() - 1);
                    setImageSwitcherAnimRight();
                    Picasso.with(EventDetailsActivity.this).load(event.getRealmImages().get(currentImagePos.get()))
                            .into(switcherPicasso);
                }
            }
        });

        activityBinding.ivNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImagePos.get() + 1 < event.getRealmImages().size()) {
                    currentImagePos.set(currentImagePos.get() + 1);

                    setImageSwitcherAnimLeft();
                    Picasso.with(EventDetailsActivity.this).load(event.getRealmImages().get(currentImagePos.get()))
                            .into(switcherPicasso);

                }
            }
        });

    }

    private void registerViewModelObservers() {

    }

    private void setUpImageSwitcher() {

        activityBinding.isImages.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
    }

    private void setImageSwitcherAnimRight() {
        Animation animIn = AnimationUtils.loadAnimation(this, R.anim.view_in_right);
        Animation animOut = AnimationUtils.loadAnimation(this, R.anim.view_out_right);

        activityBinding.isImages.setInAnimation(animIn);
        activityBinding.isImages.setOutAnimation(animOut);
    }

    private void setImageSwitcherAnimLeft() {
        Animation animIn = AnimationUtils.loadAnimation(this, R.anim.view_in_left);
        Animation animOut = AnimationUtils.loadAnimation(this, R.anim.view_out_left);

        activityBinding.isImages.setInAnimation(animIn);
        activityBinding.isImages.setOutAnimation(animOut);
    }

    private void bindEventTime(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);

        activityBinding.tvMonth.setText(dateFormat.format(date));

        dateFormat.applyPattern("dd");

        activityBinding.tvDay.setText(dateFormat.format(date));

        dateFormat.applyPattern("EEEE 'at' h:mm a");

        activityBinding.tvBeginningTime.setText(dateFormat.format(date));
    }
}
