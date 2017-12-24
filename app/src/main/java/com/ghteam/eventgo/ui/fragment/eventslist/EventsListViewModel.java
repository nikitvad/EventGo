package com.ghteam.eventgo.ui.fragment.eventslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.network.LocationFilter;
import com.ghteam.eventgo.util.LiveDataList;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

/**
 * Created by nikit on 11.12.2017.
 */

public class EventsListViewModel extends ViewModel {
    private Repository mRepository;

    private LiveDataList<Event> mEventsList;
    private MutableLiveData<OnTaskStatusChangeListener.TaskStatus> mLoadingEventsTaskStatus;


    private EventsListViewModel(Repository repository) {
        mRepository = repository;
        mEventsList = mRepository.initializeEvents();
        mLoadingEventsTaskStatus = new MutableLiveData<>();
    }


    public void loadEvents() {
        mRepository.loadEvents(new OnTaskStatusChangeListener() {
            @Override
            public void onStatusChanged(TaskStatus status) {
                mLoadingEventsTaskStatus.setValue(status);
            }
        });
    }

    public void searchEventByLocation(LocationFilter locationFilter){
        mRepository.searchEventsByLocation(locationFilter, new OnTaskStatusChangeListener() {
            @Override
            public void onStatusChanged(TaskStatus status) {
                mLoadingEventsTaskStatus.setValue(status);
            }
        });
    }

    public void loadNext() {
        mRepository.loadNextEvents(10, new OnTaskStatusChangeListener() {
            @Override
            public void onStatusChanged(TaskStatus status) {
                mLoadingEventsTaskStatus.setValue(status);
            }
        });
    }

    public LiveDataList<Event> getEventsList() {
        return mEventsList;
    }

    public MutableLiveData<OnTaskStatusChangeListener.TaskStatus> getTaskStatus() {
        return mLoadingEventsTaskStatus;
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