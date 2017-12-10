package com.ghteam.eventgo.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.data.model.Category;
import com.ghteam.eventgo.data.model.Event;
import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.data.network.CategoriesDataSource;
import com.ghteam.eventgo.data.network.EventsDataSource;
import com.ghteam.eventgo.data.network.FirebaseAccountManager;
import com.ghteam.eventgo.data.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.AccountStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class Repository {
    private static Repository sInstance;
    private static CategoriesDataSource categoriesDataSource;
    private EventsDataSource eventsDataSource;

    private static LiveData<List<Category>> eventsCategories;

    private static LiveDataList<User> mUsers;

    private static LiveDataList<Event> events;

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
        eventsDataSource = EventsDataSource.getInstance();
        mUsers = new LiveDataList<>();
    }

    public LiveData<List<Category>> getEventCategories() {

        //TODO: return data from db
        initializeCategories();
        return eventsCategories;
    }

    private void loadUsers() {
        FirebaseDatabaseManager.loadUsers(new FirebaseDatabaseManager.OnLoadUsersCompleteListener() {
            @Override
            public void onComplete(@Nullable List<User> users) {
                if (users != null && users.size() > 0) {
                    mUsers.postValue(users);
                }
            }
        });

    }

    public LiveDataList<User> getCurrentUsers() {
        loadUsers();

        return mUsers;
    }

    public void pushUser(String uid, User user, @Nullable FirebaseDatabaseManager.OnPullUserResultListener listener) {
        FirebaseDatabaseManager.pushUserInfo(uid, user, listener);
    }

    private void initializeCategories() {
        if (!isActualCategoriesList()) {
            categoriesDataSource.fetchCategories();
        }
    }

    public MutableLiveData<User> getCurrentUser() {
        return FirebaseAccountManager.getCurrentUser();
    }

    public LiveDataList<Event> getCurrentEvents() {
        return events;
    }

    public MutableLiveData<AccountStatus> getCurrentAccountStatus() {
        return FirebaseAccountManager.getCurrentAccountStatus();
    }

    public void pushNewEvent(Event event, OnSuccessListener<DocumentReference> onSuccessListener,
                             OnFailureListener onFailureListener) {
        FirebaseDatabaseManager.pushNewEvent(event, onSuccessListener, onFailureListener);
    }

    private boolean isActualCategoriesList() {
        //TODO: implement this method
        return false;
    }

    private void initializeEvents() {
        if (events == null) {
            events = eventsDataSource.loadEvents();
        }
    }
}
