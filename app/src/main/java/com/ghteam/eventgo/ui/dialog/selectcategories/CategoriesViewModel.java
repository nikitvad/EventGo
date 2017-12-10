package com.ghteam.eventgo.ui.dialog.selectcategories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.Category;

import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class CategoriesViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private Repository mRepository;
    private LiveData<List<Category>> mCategoriesList;

    public CategoriesViewModel(Repository repository) {
        mRepository = repository;

        //TODO: get list of categories
        mCategoriesList = repository.getEventCategories();

    }

    public LiveData<List<Category>> getCategoriesList() {
        return mCategoriesList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

}
