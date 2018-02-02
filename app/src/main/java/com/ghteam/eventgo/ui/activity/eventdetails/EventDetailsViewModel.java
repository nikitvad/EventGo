package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;

import java.util.List;

/**
 * Created by nikit on 13.12.2017.
 */

public class EventDetailsViewModel extends ViewModel {
    private Repository mRepository;
    private String eventId;


    private MutableLiveData<List<DiscussionMessage>> discussionMessages;
    private MutableLiveData<User> owner;

    private EventDetailsViewModel(Repository repository, final String eventId) {
        mRepository = repository;
        this.eventId = eventId;

        owner = repository.initializeUser();

    }

    public Event getEvent() {
        Event event = mRepository.getEventFromLocalDb(eventId);

        mRepository.loadUserById(event.getOwnerId());
        return event;
    }

    public MutableLiveData<List<DiscussionMessage>> getDiscussionMessages() {
        mRepository.loadNextDiscussionMessages(10);
        return discussionMessages;
    }

    public MutableLiveData<User> getOwner() {
        return owner;
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
