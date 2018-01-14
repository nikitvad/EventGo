package com.ghteam.eventgo.data_new;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data_new.task.LoadCollectionTask;
import com.ghteam.eventgo.data_new.task.LoadEventsTask;
import com.ghteam.eventgo.util.network.FirestoreUtil;

import java.util.List;

/**
 * Created by nikit on 04.01.2018.
 */

public class Repository {

    private MutableLiveData<List<Event>> events;

    private static final String TAG = Repository.class.getSimpleName();

    private static Repository sInstance;

    private Repository() {
        events = new MutableLiveData<>();
    }

    public static Repository getInstance() {
        if (sInstance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    sInstance = new Repository();
                }
            }
        }
        return sInstance;
    }

    public void loadEvents(int limit) {
       new LoadCollectionTask<Event>(Event.class, FirestoreUtil.getReferenceToEvents())
               .addTaskResultListener(new TaskResultListener<List<Event>>() {
                   @Nullable
                   @Override
                   public void onResult(List<Event> result) {
                       Log.d(TAG, "onResult: " + result);
                       events.setValue(result);
                   }
               })
               .addTaskStatusListener(new TaskStatusListener() {
                   @Override
                   public void onStatusChanged(TaskStatus status) {
                       Log.d(TAG, "onStatusChanged: " + status);

                   }
               })
       .execute(limit);
    }
}
