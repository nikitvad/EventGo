package com.ghteam.eventgo.ui.activity.eventslist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.databinding.ActivityEventsBinding;
import com.ghteam.eventgo.ui.activity.createevent.CreateEventActivity;
import com.ghteam.eventgo.ui.fragment.eventslist.EventsListFragment;
import com.ghteam.eventgo.ui.fragment.interestedevents.InterestedEventsFragment;
import com.ghteam.eventgo.ui.fragment.searchevents.SearchEventsFragment;

public class EventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventsListFragment.OnFragmentInteractionListener {

    private ActivityEventsBinding activityBinding;

    private FragmentTransaction fragmentTransaction;

    public static final String TAG = EventsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_events);

        Toolbar toolbar = activityBinding.includeToolbar.toolbar;
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

//        EventsListFragment eventsListFragment = EventsListFragment.newInstance();

//        SearchEventsFragment searchEventsFragment = SearchEventsFragment.newInstance();

        InterestedEventsFragment interestedEventsFragment = new InterestedEventsFragment();

        fragmentTransaction.replace(R.id.fl_container, interestedEventsFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_list, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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

        switch (item.getItemId()) {
            case R.id.nav_sign_out:

        }

        return true;
    }

}

