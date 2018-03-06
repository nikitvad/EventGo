package com.ghteam.eventgo.ui.fragment.eventslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.List;

/**
 * Created by nikit on 11.12.2017.
 */

public class EventsListViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<List<Event>> mEventsList;
    private MutableLiveData<TaskStatus> taskStatus;

    private static final String TAG = EventsListViewModel.class.getSimpleName();

    private EventsListViewModel(Repository repository) {
        mRepository = repository;

        mEventsList = repository.initializeEvents();
        taskStatus = repository.getLoadEventsTaskStatus();
    }

    public void loadNextEvents(int limit) {
        mRepository.loadNextEvents(limit);
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
