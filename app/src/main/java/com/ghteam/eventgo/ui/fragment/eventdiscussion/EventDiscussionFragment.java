package com.ghteam.eventgo.ui.fragment.eventdiscussion;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.databinding.FragmentEventDiscussionBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.util.InjectorUtil;

import java.util.ArrayList;
import java.util.List;


public class EventDiscussionFragment extends Fragment {

    private String mEventId;

    private static final String ARG_EVENT_ID = "arg_event_id";

    FragmentEventDiscussionBinding fragmentBinding;

    private RecyclerBindingAdapter<DiscussionMessage> recyclerBindingAdapter;

    EventDiscussionViewModel viewModel;

    public EventDiscussionFragment() {
        // Required empty public constructor
    }

    public static EventDiscussionFragment newInstance(String eventId) {
        EventDiscussionFragment fragment = new EventDiscussionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_discussion,
                container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mEventId = getArguments().getString(ARG_EVENT_ID);

            if (mEventId !=null) {

                viewModel = ViewModelProviders.of(this, InjectorUtil
                        .provideEventDiscussionViewModelFactory(getContext(), mEventId))
                        .get(EventDiscussionViewModel.class);

                registerViewModelObservers();

                recyclerBindingAdapter = new RecyclerBindingAdapter<>(R.layout.layout_discussion_message
                        , BR.message, new ArrayList<DiscussionMessage>());

                viewModel.loadNextMessages(5);

                fragmentBinding.rvMessages.setAdapter(recyclerBindingAdapter);
                fragmentBinding.rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

                fragmentBinding.rvMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (!recyclerView.canScrollVertically(1)) {
                            viewModel.loadNextMessages(5);
                        }
                    }
                });
            }
        }
    }

    private void registerViewModelObservers() {
        viewModel.getLoadMessagesTaskStatus().observe(this, new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
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

        viewModel.getMessages().observe(this, new Observer<List<DiscussionMessage>>() {
            @Override
            public void onChanged(@Nullable List<DiscussionMessage> discussionMessages) {
                Log.d("sdfsdfgsdfg", "onChanged: " + discussionMessages.size());
                recyclerBindingAdapter.addItems(discussionMessages);
            }
        });
    }

    private void showProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.GONE);
    }
}
