package com.ghteam.eventgo.data_new;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data_new.task.FirestoreCollectionLoader;
import com.ghteam.eventgo.data_new.task.TaskResultListener;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.data_new.task.TaskStatusListener;
import com.ghteam.eventgo.util.network.FirestoreUtil;

import java.util.List;

/**
 * Created by nikit on 04.01.2018.
 */

public class Repository {


    private static final String TAG = Repository.class.getSimpleName();

    private static Repository sInstance;


    private MutableLiveData<List<Event>> events;
    private MutableLiveData<TaskStatus> loadEventsTaskStatus;


    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<TaskStatus> loadCategoriesTaskStatus;

    private Repository() {

        events = new MutableLiveData<>();
        loadEventsTaskStatus = new MutableLiveData<>();

        categories = new MutableLiveData<>();
        loadCategoriesTaskStatus = new MutableLiveData<>();
    }

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

    private static int repositoryRequestCount = 0;

    public void loadEvents(int limit) {

        Log.d(TAG, "loadEvents: " + repositoryRequestCount);
        new FirestoreCollectionLoader<Event>(FirestoreUtil.getReferenceToEvents(), Event.class)
                .addTaskResultListener(new TaskResultListener<List<Event>>() {
                    @Nullable
                    @Override
                    public void onResult(List<Event> result) {
                        events.setValue(result);
                        Log.d(TAG, "onResult: " + result.size());
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        Log.d(TAG, "onStatusChanged: " + status.toString());
                        loadEventsTaskStatus.setValue(status);
                    }
                })
                .execute(limit);
    }

    public MutableLiveData<TaskStatus> initializeLoadingEventsStatus() {
        return loadEventsTaskStatus;
    }

    public MutableLiveData<List<Event>> initializeEvents() {
        return events;
    }


    private void loadCategories() {

        new FirestoreCollectionLoader<Category>(FirestoreUtil.getReferenceToCategories(), Category.class)
                .addTaskResultListener(new TaskResultListener<List<Category>>() {
                    @Nullable
                    @Override
                    public void onResult(List<Category> result) {
                        categories.setValue(result);
                        Log.d(TAG, "onResult: " + result.toString());
                    }
                })
                .addTaskStatusListener(new TaskStatusListener() {
                    @Override
                    public void onStatusChanged(TaskStatus status) {
                        loadCategoriesTaskStatus.setValue(status);
                        Log.d(TAG, "onStatusChanged: " + status);
                    }
                })
                .execute();
    }

    public MutableLiveData<List<Category>> initializeCategories() {
        if ((categories.getValue() == null || categories.getValue().size() < 1)
                && loadCategoriesTaskStatus.getValue() != TaskStatus.IN_PROGRESS) {
            loadCategories();
        }

        return categories;
    }

    public MutableLiveData<TaskStatus> initializeLoadingCategoriesTaskStatus() {
        return loadCategoriesTaskStatus;
    }
}
