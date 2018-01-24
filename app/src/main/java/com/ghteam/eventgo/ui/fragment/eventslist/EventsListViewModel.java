package com.ghteam.eventgo.ui.fragment.eventslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.entity.Event;
import com.ghteam.eventgo.data_new.network.LocationFilter;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.List;

/**
 * Created by nikit on 11.12.2017.
 */

public class EventsListViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<List<Event>> mEventsList;
    private MutableLiveData<TaskStatus> taskStatus;
    private MutableLiveData<OnTaskStatusChangeListener.TaskStatus> mLoadingEventsTaskStatus;

    private String lastLoadedEventId = "";

    private static final String TAG = EventsListViewModel.class.getSimpleName();

    private EventsListViewModel(Repository repository) {
        mRepository = repository;

        mEventsList = repository.initializeEvents();
        taskStatus = repository.getLoadEventsTaskStatus();

        mEventsList.observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (events.size() > 0) {
                    lastLoadedEventId = events.get(events.size()-1).getId();
                    Log.d(TAG, "onChanged: " + lastLoadedEventId);
                }
            }
        });
    }

    public void loadEvents(int limit) {
        mRepository.loadEvents(limit);
    }

    public void loadNextEvents(int limit) {
        mRepository.loadEventsSequentially(lastLoadedEventId, limit);
    }

    public void searchEventByLocation(LocationFilter locationFilter) {

    }

    public void loadNext() {

    }

    public MutableLiveData<List<Event>> getEventsList() {
        return mEventsList;
    }

    public MutableLiveData<TaskStatus> getTaskStatus() {
        return taskStatus;
    }

    public static class EventsListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;

        public EventsListViewModelFactory(Repository mRepository) {
            this.mRepository = mRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EventsListViewModel(mRepository);
        }
    }
}
