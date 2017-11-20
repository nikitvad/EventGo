package com.ghteam.eventgo.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.ghteam.eventgo.data.entity.CategoryEntry;
import com.ghteam.eventgo.data.network.CategoriesDataSource;

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class Repository {
    private static Repository sInstance;
    private static CategoriesDataSource categoriesDataSource;

    private static LiveData<List<CategoryEntry>> eventsCategories;

    private static Context mContext;


    public static Repository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(context);
                }
            }
        }
        return sInstance;
    }

    private Repository(Context context) {
        mContext = context;
        categoriesDataSource = CategoriesDataSource.getInstance(context);

        eventsCategories = categoriesDataSource.getCurrentCategories();
    }


    public void createNewUser() {

    }


    public LiveData<List<CategoryEntry>> getEventCategories() {

        //TODO: return data from db
        initializeCategories();
        return eventsCategories;
    }


    private void initializeCategories() {
        if (!isActualCategoriesList()) {
                categoriesDataSource.fetchCategories();
        }
    }

    private boolean isActualCategoriesList() {

        //TODO: implement this method
        return false;
    }

    private void startFetchCategoriesService() {

    }
}
