package com.ghteam.eventgo.ui.dialog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.CategoryEntry;

import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class CategoriesViewModel extends ViewModel {
    private boolean isLoading = false;
    private Repository mRepository;
    private LiveData<List<CategoryEntry>> mCategoriesList;

    public CategoriesViewModel(Repository repository) {
        mRepository = repository;

        //TODO: get list of categories
        mCategoriesList = repository.getEventCategories();

    }

    public LiveData<List<CategoryEntry>> getCategoriesList() {
        return mCategoriesList;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

}
