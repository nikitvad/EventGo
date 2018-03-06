package com.ghteam.eventgo.ui.fragment.usersGoingEvents.interestedevents;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.databinding.FragmentInterestedEventsBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.util.InjectorUtil;

import java.util.ArrayList;
import java.util.List;


public class GoingEventsFragment extends Fragment {

    private FragmentInterestedEventsBinding fragmentBinding;
    private InterestedEventsViewModel viewModel;
    private RecyclerBindingAdapter<Event> recyclerBindingAdapter;

    public GoingEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this, InjectorUtil
                .provideInterestedEventViewModelFactory(getActivity().getApplicationContext()))
                .get(InterestedEventsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_interested_events,
                container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerBindingAdapter = new RecyclerBindingAdapter<>(R.layout.layout_event_list_item_v2, BR.event,
                new ArrayList<Event>());

        fragmentBinding.rvEventsList.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvEventsList.setAdapter(recyclerBindingAdapter);
        registerViewModelObservers();
        Log.d("ddfgfdgs", "onViewCreated: ");
        viewModel.startLoadingNextEvents(10);
    }

    private void registerViewModelObservers() {
        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                recyclerBindingAdapter.addItems(events);
            }
        });

        viewModel.getLoadingEventsTaskStatus().observe(this, new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                if (taskStatus == TaskStatus.IN_PROGRESS) {
                    fragmentBinding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    fragmentBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
