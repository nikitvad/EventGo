package com.ghteam.eventgo.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;

import com.ghteam.eventgo.AppExecutors;
import com.ghteam.eventgo.data.database.CategoryDao;
import com.ghteam.eventgo.data.database.EventDao;
import com.ghteam.eventgo.data.database.ImageDao;
import com.ghteam.eventgo.data.database.ImageEntry;
import com.ghteam.eventgo.data.database.LocationDao;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.Location;
import com.ghteam.eventgo.data.entity.User;
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

import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class Repository {

    private static Repository sInstance;

    private final EventDao eventDao;
    private final CategoryDao categoryDao;
    private final ImageDao imageDao;
    private final LocationDao locationDao;

    private final AppExecutors appExecutors;

    private static CategoriesDataSource categoriesDataSource;
    private static EventsDataSource eventsDataSource;
    private static UsersDataSource usersDataSource;

    private static LiveData<List<Category>> eventsCategories;

    private static LiveDataList<User> mUsers;

    private static LiveDataList<Event> events;

    public static Repository getInstance(Context context, AppExecutors executors, EventDao eventDao, CategoryDao categoryDao,
                                         ImageDao imageDao, LocationDao locationDao) {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository(executors, eventDao, categoryDao, imageDao, locationDao);
                }
            }
        }
        return sInstance;
    }

    private Repository(AppExecutors executors, final EventDao eventDao, final CategoryDao categoryDao,
                       final ImageDao imageDao, LocationDao locationDao) {

        this.appExecutors = executors;

        this.eventDao = eventDao;
        this.categoryDao = categoryDao;
        this.imageDao = imageDao;
        this.locationDao = locationDao;

        categoriesDataSource = CategoriesDataSource.getInstance();
        eventsDataSource = EventsDataSource.getInstance();
        usersDataSource = UsersDataSource.getInstance();

        events = eventsDataSource.getCurrentEvents();
        //TODO: finish this block
        events.observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {
                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        imageDao.deleteAll();
                        categoryDao.deleteAll();
                        eventDao.deleteAll();


                        saveEventsToDb(events);
                    }
                });
            }
        });
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

    public MutableLiveData<User> initializeUser(){
        return usersDataSource.getCurrentUser();
    }

    public void loadUserById(String id, OnTaskStatusChangeListener listener) {
        usersDataSource.loadUserById(id, listener);
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

    public void pushNewEvent(Event event, OnSuccessListener<Void> onSuccessListener,
                             OnFailureListener onFailureListener) {
        FirebaseDatabaseManager.pushNewEvent(event, onSuccessListener, onFailureListener);
    }

    public void loadEvents(OnTaskStatusChangeListener listener) {
        eventsDataSource.loadEvents(listener);
    }

    public LiveData<Event> getEventById(String id) {
        return eventDao.findEventById(id);
    }

    public void loadNextEvents(int count, OnTaskStatusChangeListener listener) {
        eventsDataSource.loadNextEvents(count, listener);
    }

    private void saveEventsToDb(List<Event> events) {

        eventDao.insertAll(events);

        for (Event item : events) {

            Category category = item.getCategory();
            category.setOwnerId(item.getId());

            categoryDao.insertAll(category);
            for (String image : item.getImages()) {
                imageDao.insertAll(new ImageEntry(item.getId(), image));
            }
        }
    }

    public LiveDataList<Event> initializeEvents() {
        if (events == null) {
            events = eventsDataSource.getCurrentEvents();
        }
        return events;
    }

    public LiveData<List<ImageEntry>> getImagesByOwner(String id) {
        return imageDao.findImagesByOwner(id);
    }

    public LiveData<Category> getCategoryByOwner(String id) {
        return categoryDao.findCategoryByOwner(id);
    }

    public LiveData<Location> getLocationByOwner(String id) {
        return locationDao.findLocationByOwnerId(id);
    }

}
