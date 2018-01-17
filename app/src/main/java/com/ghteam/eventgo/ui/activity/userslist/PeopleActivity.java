package com.ghteam.eventgo.ui.activity.userslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ghteam.eventgo.R;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.databinding.ActivityPeopleBinding;

import java.util.List;

public class PeopleActivity extends AppCompatActivity {

    private PeopleViewModel viewModel;

    private ActivityPeopleBinding activityBinding;

    private PeopleRecyclerAdapter usersAdapter;

    private RecyclerView rvUsers;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_people);

//        viewModel = ViewModelProviders.of(this, new PeopleViewModel
//                .UsersViewModelFactory(InjectorUtil.provideRepository(this))).get(PeopleViewModel.class);
        viewModel = ViewModelProviders.of(this, new PeopleViewModel.UsersViewModelFactory(Repository.getInstance(this)))
                .get(PeopleViewModel.class);
        usersAdapter = new PeopleRecyclerAdapter(this);

        rvUsers = activityBinding.rvUsers;
        progressBar = activityBinding.progressBar;

        rvUsers.setAdapter(usersAdapter);
        rvUsers.setLayoutManager(new GridLayoutManager(this, 3));

        registerViewModelObservers();

        viewModel.startLoading();
    }


    private void registerViewModelObservers() {
        viewModel.getUsers().observeForever(new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                usersAdapter.setItems(users);
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
}
