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
import android.widget.EditText;

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
import java.util.List;


public class EventDiscussionFragment extends Fragment {

    private String mEventId;

    private static final String ARG_EVENT_ID = "arg_event_id";

    public static final String TAG = EventDiscussionFragment.class.getSimpleName();

    private FragmentEventDiscussionBinding fragmentBinding;

    private RecyclerBindingAdapter<DiscussionMessage> recyclerBindingAdapter;

    private EventDiscussionClient eventDiscussionClient;

    private RecyclerView rvMessages;
    private EditText etNewMessage;

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

            rvMessages = fragmentBinding.rvMessages;
            etNewMessage = fragmentBinding.etNewMessage;
            mEventId = getArguments().getString(ARG_EVENT_ID);

            if (mEventId != null) {

                fragmentBinding.setVariable(BR.userProfilePicture, PrefsUtil.getUserProfilePicture());

                recyclerBindingAdapter = new RecyclerBindingAdapter<>(R.layout.layout_discussion_message,
                        BR.message, new ArrayList<DiscussionMessage>());

                rvMessages.setAdapter(recyclerBindingAdapter);
                rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

                eventDiscussionClient = new EventDiscussionClient(mEventId);
                eventDiscussionClient.setDiscussionListener(new EventDiscussionClient.DiscussionListener() {
                    @Override
                    public void onAddedMessages(List<DiscussionMessage> newMessages) {

                        recyclerBindingAdapter.addItems(newMessages);
                        rvMessages.scrollToPosition(recyclerBindingAdapter.getItemCount() - 1);

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

                fragmentBinding.ivSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNewMessage.length() > 0) {
                            eventDiscussionClient.sendMessage(etNewMessage.getText().toString(), new TaskStatusListener() {
                                @Override
                                public void onStatusChanged(TaskStatus status) {
                                    if (status == TaskStatus.IN_PROGRESS) {
                                        fragmentBinding.sendMessageProgress.setVisibility(View.VISIBLE);
                                        fragmentBinding.ivSend.setVisibility(View.GONE);
                                    } else if (status == TaskStatus.SUCCESS) {
                                        etNewMessage.setText("");
                                        fragmentBinding.sendMessageProgress.setVisibility(View.GONE);
                                        fragmentBinding.ivSend.setVisibility(View.VISIBLE);
                                    }
                                    //TODO: display task result (progress bar or message about error)
                                }
                            });
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
