package com.ghteam.eventgo.ui.activity.userslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.databinding.ActivityUsersBinding;

import java.util.List;

public class PeopleActivity extends AppCompatActivity {

    private PeopleViewModel viewModel;

    private ActivityUsersBinding activityBinding;

    private PeopleRecyclerAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);

        viewModel = ViewModelProviders.of(this, new PeopleViewModel
                .UsersViewModelFactory(Repository.getInstance(this))).get(PeopleViewModel.class);

        usersAdapter = new PeopleRecyclerAdapter(this);

        activityBinding.rvUsers.setAdapter(usersAdapter);
        activityBinding.rvUsers.setLayoutManager(new GridLayoutManager(this, 3));

        registerViewModelObservers();
    }


    private void registerViewModelObservers() {
        viewModel.getUsers().observeForever(new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                usersAdapter.setItems(users);
            }
        });
    }
}
