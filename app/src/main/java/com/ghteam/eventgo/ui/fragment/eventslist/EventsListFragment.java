package com.ghteam.eventgo.ui.fragment.eventslist;

import android.Manifest;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.databinding.FragmentEventsListBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.ui.activity.eventdetails.EventDetailsActivity;
import com.ghteam.eventgo.util.InjectorUtil;
import com.ghteam.eventgo.util.network.LocationUtil;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class EventsListFragment extends Fragment {

    private static final String TAG = EventsListFragment.class.getSimpleName();

    private static final int REQUEST_CODE_REQUEST_PERMISSIONS = 1111;

    private FragmentEventsListBinding mFragmentBinding;

    private OnFragmentInteractionListener mListener;

    private EventsListViewModel mViewModel;
    private RecyclerBindingAdapter<Event> mRecyclerAdapter;

    private LocationManager mLocationManager;
    private Location mCurrentLocation;

    public EventsListFragment() {
        // Required empty public constructor
    }


    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_events_list, container, false);

        return mFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventsListViewModel.EventsListViewModelFactory viewModelFactory = InjectorUtil
                .provideEventsListViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventsListViewModel.class);

        mRecyclerAdapter = new RecyclerBindingAdapter<>(R.layout.layout_event_list_item_v2,
                BR.event, new ArrayList<Event>());

        mRecyclerAdapter.setOnItemClickListener(new RecyclerBindingAdapter.OnItemClickListener<Event>() {
            @Override
            public void onItemClick(int position, Event item) {
                Intent intentEventDetails = new Intent(getContext(), EventDetailsActivity.class);
                intentEventDetails.putExtra("eventId", item.getId());
                startActivity(intentEventDetails);
            }
        });

        mFragmentBinding.rvEventsList.setAdapter(mRecyclerAdapter);

        mFragmentBinding.rvEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        registerViewModelObservers();
        mViewModel.loadNext();

        updateLocation();


    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            mCurrentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider + " " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }
    };

    private void updateLocation() {

        int checkFineLocationPermission = ActivityCompat
                .checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int checkCoarseLocationPermission = ActivityCompat
                .checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (checkCoarseLocationPermission != PackageManager.PERMISSION_GRANTED
                && checkFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };

            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_REQUEST_PERMISSIONS);

        } else {

            mCurrentLocation = LocationUtil.getLastKnownLocation(mLocationManager, null);

            if(mCurrentLocation!=null) {
                Log.d(TAG, "updateLocation: " + mCurrentLocation.toString());
            }
//            mLocationManager.requestSingleUpdate();

        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_REQUEST_PERMISSIONS && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void registerViewModelObservers() {
        mViewModel.getEventsList().observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                mRecyclerAdapter.addItems(events);
            }
        });

        mViewModel.getTaskStatus().observeForever(new Observer<OnTaskStatusChangeListener.TaskStatus>() {
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
        mFragmentBinding.progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mFragmentBinding.rvEventsList.setAlpha(0.5f);
    }


    private void hideProgressBar() {
        mFragmentBinding.progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mFragmentBinding.rvEventsList.setAlpha(1f);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
