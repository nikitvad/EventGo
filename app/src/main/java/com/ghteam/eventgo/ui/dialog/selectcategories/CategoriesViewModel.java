package com.ghteam.eventgo.ui.dialog.selectcategories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.task.TaskStatus;

import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class CategoriesViewModel extends ViewModel {
    private MutableLiveData<TaskStatus> taskStatus;
    private Repository mRepository;
    private LiveData<List<Category>> mCategoriesList;

    public CategoriesViewModel(Repository repository) {
        mRepository = repository;

        //TODO: get list of categories
        mCategoriesList = repository.initializeCategories();
        taskStatus = repository.getLoadCategoriesTaskStatus();

    }

    public LiveData<List<Category>> getCategoriesList() {
        return mCategoriesList;
    }

    public LiveData<TaskStatus> getLoadingTaskStatus() {
        return taskStatus;
    }


}
