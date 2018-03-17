package com.ghteam.eventgo.ui.activity.userslist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.entity.UserLocationInfo;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.databinding.ActivityInviteUsersBinding;
import com.ghteam.eventgo.ui.dialog.DistanceFilterDialog;
import com.ghteam.eventgo.util.InjectorUtil;

import java.util.ArrayList;
import java.util.List;

import static butterknife.internal.Utils.arrayOf;

public class InviteUsersActivity extends AppCompatActivity implements View.OnClickListener {

    private PeopleViewModel viewModel;

    private ActivityInviteUsersBinding activityBinding;
    private RecyclerView rvUsers;
    private ProgressBar progressBar;
    private Snackbar snackbarGettingCurrentLocation;

    private LocationManager locationManager;
    private Location currentLocation;

    private DistanceFilterDialog distanceFilterDialog;

    private InviteUsersRecyclerAdapter inviteUsersRecyclerAdapter;

    private ArrayList<String> invitedUsers;

    private int currentSearchAreaSize = 3;

    private static final int REQUEST_PERMISSION_CODE_LOCATION = 3001;

    public static final String EXTRA_INVITED_USERS = "invited_users";

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            snackbarGettingCurrentLocation.dismiss();
            if (currentLocation != null && currentLocation.distanceTo(location) > 2000) {
                inviteUsersRecyclerAdapter.removeAllItems();
                currentLocation = location;
                startSearch(location, currentSearchAreaSize);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_invite_users);
        setSupportActionBar(activityBinding.toolbar2);

        activityBinding.btSubmit.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        snackbarGettingCurrentLocation = Snackbar.make(activityBinding.getRoot(), "Getting current location...",
                BaseTransientBottomBar.LENGTH_INDEFINITE);

        viewModel = ViewModelProviders.of(this, InjectorUtil.provideUsersViewModelFactory(this))
                .get(PeopleViewModel.class);
//        usersAdapter = new InviteUsersRecyclerAdapter(this);

        distanceFilterDialog = new DistanceFilterDialog();
        distanceFilterDialog.setOnDistanceFilterChanged(new DistanceFilterDialog.OnDistanceFilterChangeListener() {
            @Override
            public void onDistanceChanged(int value) {
                currentSearchAreaSize = value;
                if (currentLocation != null) {
                    startSearch(currentLocation, currentSearchAreaSize);
                }
            }
        });

        rvUsers = activityBinding.rvUsers;
        progressBar = activityBinding.progressBar;

        inviteUsersRecyclerAdapter = new InviteUsersRecyclerAdapter(new ArrayList<UserLocationInfo>());
        inviteUsersRecyclerAdapter.setOnInviteClickListener(new InviteUsersRecyclerAdapter.OnInviteClickListener() {
            @Override
            public void onInviteClickListener(View v, UserLocationInfo userLocationInfo) {
                if (invitedUsers.contains(userLocationInfo.getUserId())) {
                    invitedUsers.remove(userLocationInfo.getUserId());
                } else {
                    invitedUsers.add(userLocationInfo.getUserId());
                }
            }
        });

        rvUsers.setAdapter(inviteUsersRecyclerAdapter);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        registerViewModelObservers();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_CODE_LOCATION);
        } else {

            Intent intent = new Intent();
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
            snackbarGettingCurrentLocation.show();

            if (currentLocation != null) {
                inviteUsersRecyclerAdapter.setCurrentLocation(new AppLocation(currentLocation.getLatitude()
                        , currentLocation.getLongitude()));
                startSearch(currentLocation, currentSearchAreaSize);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        invitedUsers = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invite_users_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_distance_filter) {
            distanceFilterDialog.show(getFragmentManager(), "TAG");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_LOCATION) {

            boolean requestResult = true;

            for (int item : grantResults) {
                if (item != PackageManager.PERMISSION_GRANTED) {
                    requestResult = false;
                }
            }

            if (requestResult) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                snackbarGettingCurrentLocation.show();
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                if (currentLocation != null) {
                    inviteUsersRecyclerAdapter.setCurrentLocation(new AppLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    startSearch(currentLocation, currentSearchAreaSize);
                }
            }

        }
    }

    private void startSearch(Location location, int areaSize) {
        inviteUsersRecyclerAdapter.removeAllItems();
        viewModel.startLoading(new AppLocation(location.getLatitude(), location.getLongitude()), areaSize * 2, areaSize * 2);
    }

    private void registerViewModelObservers() {
        viewModel.getUsers().observeForever(new Observer<List<UserLocationInfo>>() {
            @Override
            public void onChanged(@Nullable List<UserLocationInfo> users) {
                inviteUsersRecyclerAdapter.addItems(users);
            }
        });

        viewModel.getTaskStatus().observeForever(new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                switch (taskStatus) {
                    case IN_PROGRESS:
                        showProgressBar();
                        return;
                    default:
                        hideProgressBar();
                }
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        rvUsers.setAlpha(0.5f);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        rvUsers.setAlpha(1f);
    }

    private void submit() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_INVITED_USERS, invitedUsers);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                submit();
        }
    }
}
