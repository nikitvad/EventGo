package com.ghteam.eventgo.ui.activity.eventslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.databinding.ActivityEventsListBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.ui.activity.createevent.CreateEventActivity;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityEventsListBinding activityBinding;
    private EventsListViewModel viewModel;
    private RecyclerBindingAdapter<Event> recyclerAdapter;

    private RecyclerView rvEventsList;

    private ProgressBar progressBar;

    public static final String TAG = EventsListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_events_list);

        viewModel = ViewModelProviders.of(this, new EventsListViewModel
                .EventsListViewModelFactory(Repository.getInstance(this)))
                .get(EventsListViewModel.class);

        recyclerAdapter = new RecyclerBindingAdapter<>(R.layout.layout_event_list_item,
                BR.event, new ArrayList<Event>());

        rvEventsList = activityBinding.content.content.rvEventsList;
        rvEventsList.setAdapter(recyclerAdapter);
        rvEventsList.setLayoutManager(new GridLayoutManager(this, 2));

        rvEventsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadEvents();
                }
            }
        });

        progressBar = activityBinding.content.content.progressBar;

        Toolbar toolbar = activityBinding.content.toolbar;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = activityBinding.content.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEventIntent = new Intent(EventsListActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registerViewModelObservers();
        viewModel.loadEvents();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void registerViewModelObservers() {
        viewModel.getEventsList().observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                recyclerAdapter.addItems(events);
            }
        });

        viewModel.getTaskStatus().observeForever(new Observer<OnTaskStatusChangeListener.TaskStatus>() {
            @Override
            public void onChanged(@Nullable OnTaskStatusChangeListener.TaskStatus taskStatus) {
                switch (taskStatus) {
                    case IN_PROGRESS:
                        showProgressBar();
                        return;
                    default:
                        hideProgressBar();
                        return;
                }
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        rvEventsList.setAlpha(0.5f);
    }


    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        rvEventsList.setAlpha(1f);
    }
}

