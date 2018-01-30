package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
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
import com.ghteam.eventgo.data.database.ImageEntry;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.Location;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.databinding.ActivityEventDetailsBinding;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsViewModel.EventDetailsViewModelFactory;
import com.ghteam.eventgo.util.ImageSwitcherPicasso;
import com.ghteam.eventgo.util.InjectorUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;

public class EventDetailsActivity extends AppCompatActivity {

    private ActivityEventDetailsBinding activityBinding;

    private EventDetailsViewModel viewModel;

    public static final String TAG = EventDetailsActivity.class.getSimpleName();


    private ImageSwitcherPicasso switcherPicasso;

    private int currentImagePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_details);

        currentImagePos = 0;

        String eventId = getIntent().getStringExtra("eventId");


        EventDetailsViewModelFactory viewModelFactory = InjectorUtil
                .provideEventDetailsViewModelFactory(this, "1DaM4uttOQV6EHYPwnMu");


        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(EventDetailsViewModel.class);

        RealmQuery realmQuery = Realm.getDefaultInstance().where(Event.class);

        Event event = (Event) realmQuery.findFirst();

        Log.d(TAG, "onCreate: " + event.getImages().size());

        setUpImageSwitcher();

        switcherPicasso = new ImageSwitcherPicasso(EventDetailsActivity.this, activityBinding.isImages);

        activityBinding.ivNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImagePos + 1 < viewModel.getImages().getValue().size()) {
                    currentImagePos++;
                    setImageSwitcherAnimLeft();
                    Picasso.with(EventDetailsActivity.this).load(viewModel.getImages().getValue().get(currentImagePos).url)
                            .into(switcherPicasso);
                }
            }
        });

        activityBinding.ivPreviousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImagePos - 1 >= 0) {
                    currentImagePos--;
                    setImageSwitcherAnimRight();
                    Picasso.with(EventDetailsActivity.this).load(viewModel.getImages().getValue().get(currentImagePos).url)
                            .into(switcherPicasso);
                }
            }
        });

//        registerViewModelObservers();
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

    private void registerViewModelObservers() {
        viewModel.getEvent().observeForever(new Observer<Event>() {
            @Override
            public void onChanged(@Nullable Event event) {
                activityBinding.setVariable(BR.event, event);
                bindEventTime(event.getDate());
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

        viewModel.getImages().observeForever(new Observer<List<ImageEntry>>() {
            @Override
            public void onChanged(@Nullable List<ImageEntry> imageEntries) {

                for (ImageEntry item : imageEntries) {
                    Log.d(TAG, "onChanged: " + item.url);
                }

                if (imageEntries.size() > 0) {
                    activityBinding.lfImages.setVisibility(View.VISIBLE);
                    if (currentImagePos < imageEntries.size()) {
                        Picasso.with(EventDetailsActivity.this).load(imageEntries.get(currentImagePos).url)
                                .into(switcherPicasso);
                    }
                } else {
                    activityBinding.lfImages.setVisibility(View.GONE);
                }
            }
        });
    }
}
