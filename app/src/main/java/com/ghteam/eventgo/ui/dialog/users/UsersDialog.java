package com.ghteam.eventgo.ui.dialog.users;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghteam.eventgo.BR;
import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.task.FetchUsersFromCollection;
import com.ghteam.eventgo.data.task.TaskResultListener;
import com.ghteam.eventgo.databinding.DialogUsersBinding;
import com.ghteam.eventgo.ui.RecyclerBindingAdapter;
import com.ghteam.eventgo.util.network.FirestoreUtil;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

/**
 * Created by nikit on 22.02.2018.
 */

public class UsersDialog extends DialogFragment {

    private DialogUsersBinding dialogBinding;
    private RecyclerView rvUsers;

    private RecyclerBindingAdapter<User> userRecyclerBindingAdapter;

    private static final String TAG = UsersDialog.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        dialogBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_users, container, false);

        return dialogBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUsers = dialogBinding.rvUsers;

        userRecyclerBindingAdapter = new RecyclerBindingAdapter<>(R.layout.users_list_item,
                BR.user, new ArrayList<User>());

        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        rvUsers.setAdapter(userRecyclerBindingAdapter);

        FetchUsersFromCollection fetchUsersFromCollection = new FetchUsersFromCollection();

        CollectionReference collectionReference = FirestoreUtil.getReferenceToEvents()
                .document("qmYHsryGnznAm0f7YrsC")
                .collection("going");

        fetchUsersFromCollection.addTaskResultListener(new TaskResultListener<User>() {
            @Override
            public void onResult(User result) {
                userRecyclerBindingAdapter.addItem(result);
            }
        });


        fetchUsersFromCollection.execute(collectionReference);

    }
}
