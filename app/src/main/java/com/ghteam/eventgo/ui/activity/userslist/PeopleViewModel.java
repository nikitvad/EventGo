package com.ghteam.eventgo.ui.activity.userslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.task.TaskStatus;

import java.util.List;

/**
 * Created by nikit on 08.12.2017.
 */

public class PeopleViewModel extends ViewModel {
    private Repository mRepository;
    private MutableLiveData<List<User>> mUsers;

    private MutableLiveData<TaskStatus> taskStatus;

    private PeopleViewModel(Repository repository) {
        mRepository = repository;

        mUsers = mRepository.initializeUsers();

        taskStatus = mRepository.getLoadUsersTaskStatus();

    }

    public void startLoading(){
        mRepository.loadUsers(20);
    }

    public MutableLiveData<List<User>> getUsers() {
        return mUsers;
    }

    public MutableLiveData<TaskStatus> getTaskStatus() {
        return taskStatus;
    }

    public static class UsersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;

        public UsersViewModelFactory(Repository mRepository) {
            this.mRepository = mRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PeopleViewModel(mRepository);
        }
    }
}
