package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;

/**
 * Created by nikit on 20.11.2017.
 */


public class ProfileSettingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository mRepository;


    public ProfileSettingViewModelFactory(Repository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileSettingsViewModel(mRepository);
    }
}

