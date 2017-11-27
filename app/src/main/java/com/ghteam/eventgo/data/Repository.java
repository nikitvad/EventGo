package com.ghteam.eventgo.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.network.CategoriesDataSource;
import com.ghteam.eventgo.data.network.FirebaseAccountManager;
import com.ghteam.eventgo.data.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.util.network.AccountStatus;

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class Repository {
    private static Repository sInstance;
    private static CategoriesDataSource categoriesDataSource;

    private static LiveData<List<Category>> eventsCategories;

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

    public LiveData<List<Category>> getEventCategories() {

        //TODO: return data from db
        initializeCategories();
        return eventsCategories;
    }

    public void pullUser(String uid, User user, @Nullable FirebaseDatabaseManager.OnPullUserResultListener listener) {


        FirebaseDatabaseManager.pullUserInfo(uid, user, listener);
    }

    private void initializeCategories() {
        if (!isActualCategoriesList()) {
            categoriesDataSource.fetchCategories();
        }
    }

    public MutableLiveData<User> getCurrentUser() {
        return FirebaseAccountManager.getCurrentUser();
    }

    public MutableLiveData<AccountStatus> getCurrentAccountStatus() {
        return FirebaseAccountManager.getCurrentAccountStatus();
    }


    private boolean isActualCategoriesList() {
        //TODO: implement this method
        return false;
    }
}
