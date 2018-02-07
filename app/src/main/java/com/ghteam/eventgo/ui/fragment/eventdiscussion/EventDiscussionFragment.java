package com.ghteam.eventgo.ui.fragment.eventdiscussion;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.network.EventDiscussionClient;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.data.task.TaskStatusListener;
import com.ghteam.eventgo.databinding.FragmentEventDiscussionBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.util.PrefsUtil;

import java.util.ArrayList;


public class EventDiscussionFragment extends Fragment {

    private String mEventId;

    private static final String ARG_EVENT_ID = "arg_event_id";

    FragmentEventDiscussionBinding fragmentBinding;

    private RecyclerBindingAdapter<DiscussionMessage> recyclerBindingAdapter;

    private EventDiscussionClient eventDiscussionClient;

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

            if (mEventId != null) {


                fragmentBinding.setVariable(BR.userProfilePicture, PrefsUtil.getUserProfilePicture());

                recyclerBindingAdapter = new RecyclerBindingAdapter<>(R.layout.layout_discussion_message
                        , BR.message, new ArrayList<DiscussionMessage>());


                fragmentBinding.rvMessages.setAdapter(recyclerBindingAdapter);
                fragmentBinding.rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

                eventDiscussionClient = new EventDiscussionClient(mEventId);
                eventDiscussionClient.setDiscussionListener(new EventDiscussionClient.DiscussionListener() {
                    @Override
                    public void onAddedMessages(DiscussionMessage newMessages) {
                        recyclerBindingAdapter.addItem(newMessages);
                    }

                    @Override
                    public void onMessagesRemoved(DiscussionMessage discussionMessage) {
                        recyclerBindingAdapter.removeItem(discussionMessage);
                    }
                });

                eventDiscussionClient.setTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        if (status == TaskStatus.IN_PROGRESS) {
                            showProgressBar();
                        } else {
                            hideProgressBar();
                        }
                    }
                });

                eventDiscussionClient.loadDiscussion(5);

                fragmentBinding.rvMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (!recyclerView.canScrollVertically(1)) {
                            eventDiscussionClient.loadNextMessages(5);
                        }
                    }
                });

            }
        }
    }

    private void showProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        fragmentBinding.progressBar.setVisibility(View.GONE);
    }
}
