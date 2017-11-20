package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.CategoryEntry;

import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class ProfileSettingsViewModel extends ViewModel {
    private Repository mRepository;
    private LiveData<List<CategoryEntry>> mCategoriesList;

    public ProfileSettingsViewModel(Repository repository){
        mRepository = repository;

        //TODO: get list of categories
        mCategoriesList = repository.getEventCategories();
    }

    public LiveData<List<CategoryEntry>> getCategoriesList() {
        return mCategoriesList;
    }



}
