package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;

/**
 * Created by nikit on 13.12.2017.
 */

public class EventDetailsViewModel extends ViewModel {
    private Repository mRepository;
    private String eventId;

    private MutableLiveData<User> user;

    private EventDetailsViewModel(Repository repository, final String eventId) {
        mRepository = repository;
        this.eventId = eventId;

        user = repository.initializeUser();
    }

    public Event getEvent() {
        Event event =  mRepository.getEventFromLocalDb(eventId);

        mRepository.loadUserById(event.getOwnerId());
        return event;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public static class EventDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;
        private String eventId;

        public EventDetailsViewModelFactory(Repository repository, String eventId) {
            this.mRepository = repository;
            this.eventId = eventId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EventDetailsViewModel(mRepository, eventId);
        }
    }
}
