package com.ghteam.eventgo.ui.activity.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.Repository;

/**
 * Created by nikit on 17.11.2017.
 */

public class LoginViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRequireUpdateProfile;


    private final static String TAG = LoginViewModel.class.getSimpleName();

    private LoginViewModel(Repository repository) {
        this.mRepository = repository;

        isRequireUpdateProfile = repository.getIsRequireUpdateProfile();

        isRequireUpdateProfile.observeForever(new Observer<Boolean>() {

            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null) {
                    isLoading.setValue(true);
                } else {
                    isLoading.setValue(false);
                }
            }
        });

    }

    MutableLiveData<Boolean> getIsRequireUpdateProfile() {
        return isRequireUpdateProfile;
    }

    MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public static class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;

        public LoginViewModelFactory(Repository mRepository) {
            this.mRepository = mRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LoginViewModel(mRepository);
        }
    }
}
