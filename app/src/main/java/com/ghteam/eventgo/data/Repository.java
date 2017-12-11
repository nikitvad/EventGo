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
import com.ghteam.eventgo.data.network.UsersDataSource;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.AccountStatus;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;
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
    private static EventsDataSource eventsDataSource;
    private static UsersDataSource usersDataSource;

    private static LiveData<List<Category>> eventsCategories;

    private static LiveDataList<User> mUsers;

    private static LiveDataList<Event> events;

    public static Repository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository();
                }
            }
        }
        return sInstance;
    }

    private Repository() {
        categoriesDataSource = CategoriesDataSource.getInstance();
        eventsDataSource = EventsDataSource.getInstance();
        usersDataSource = UsersDataSource.getInstance();
    }

    public void loadUsers(OnTaskStatusChangeListener listener) {
        usersDataSource.loadUsers(listener);
    }

    public LiveDataList<User> initializeUsers() {
        if (mUsers == null) {
            mUsers = usersDataSource.getCurrentUsers();
        }
        return mUsers;
    }

    public void pushUser(String uid, User user, @Nullable FirebaseDatabaseManager.OnPullUserResultListener listener) {
        FirebaseDatabaseManager.pushUserInfo(uid, user, listener);
    }

    public LiveData<List<Category>> initializeCategories() {
        if (eventsCategories == null) {
            eventsCategories = categoriesDataSource.getCurrentCategories();
            categoriesDataSource.loadCategories();
        }
        return eventsCategories;
    }
    
    public MutableLiveData<User> getCurrentAccount() {
        return FirebaseAccountManager.getCurrentUser();
    }

    public MutableLiveData<AccountStatus> getCurrentAccountStatus() {
        return FirebaseAccountManager.getCurrentAccountStatus();
    }

    public void pushNewEvent(Event event, OnSuccessListener<DocumentReference> onSuccessListener,
                             OnFailureListener onFailureListener) {
        FirebaseDatabaseManager.pushNewEvent(event, onSuccessListener, onFailureListener);
    }

    public void loadEvents(OnTaskStatusChangeListener listener) {
        eventsDataSource.loadEvents(listener);
    }

    public LiveDataList<Event> initializeEvents() {
        if (events == null) {
            events = eventsDataSource.getCurrentEvents();
        }
        return events;
    }
}
