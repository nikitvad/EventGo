package com.ghteam.eventgo.ui.activity.userslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

/**
 * Created by nikit on 08.12.2017.
 */

public class PeopleViewModel extends ViewModel {
    private Repository mRepository;
    private LiveDataList<User> mUsers;

    private MutableLiveData<Boolean> mIsLoading;

    private PeopleViewModel(Repository repository) {
        mRepository = repository;

        mUsers = mRepository.initializeUsers();

        mIsLoading = new MutableLiveData<>();
        mRepository.loadUsers(new OnTaskStatusChangeListener() {
            @Override
            public void onStatusChanged(TaskStatus status) {
                switch (status) {
                    case IN_PROGRESS:
                        mIsLoading.setValue(true);
                        return;
                    default:
                        mIsLoading.setValue(false);
                        return;
                }
            }
        });
    }

    public LiveDataList<User> getUsers() {
        return mUsers;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    static class UsersViewModelFactory extends ViewModelProvider.NewInstanceFactory {
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
