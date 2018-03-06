package com.ghteam.eventgo.ui.fragment.interestedevents;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.task.TaskStatus;

import java.util.List;

/**
 * Created by nikit on 04.03.2018.
 */

public class InterestedEventsViewModel extends ViewModel {

    private MutableLiveData<List<Event>> events;
    private MutableLiveData<TaskStatus> loadingEventsTaskStatus;
    private Repository repository;


    private InterestedEventsViewModel(Repository repository) {
        this.repository = repository;
        events = repository.getUsersInterestedEvents();
        loadingEventsTaskStatus = repository.getLoadingUsersInterestedEventsTaskStatus();
    }

    public void startLoadingNextEvents(int countLimit) {
        repository.loadNextUsersInterestedEvents(countLimit);
    }

    public MutableLiveData<List<Event>> getEvents() {
        return events;
    }

    public MutableLiveData<TaskStatus> getLoadingEventsTaskStatus() {
        return loadingEventsTaskStatus;
    }

    public static class InterestedEventsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;

        public InterestedEventsViewModelFactory(Repository mRepository) {
            this.mRepository = mRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new InterestedEventsViewModel(mRepository);
        }
    }
}
