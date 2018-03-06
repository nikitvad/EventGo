package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Event;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.task.TaskResultListener;

/**
 * Created by nikit on 13.12.2017.
 */

public class EventDetailsViewModel extends ViewModel {
    private Repository mRepository;
    private String eventId;

    private Event event;

    private MutableLiveData<User> owner;
    private MutableLiveData<Boolean> isInterestedByUser;
    private MutableLiveData<Boolean> isUserGoing;

    private EventDetailsViewModel(Repository repository, final String eventId) {
        mRepository = repository;
        this.eventId = eventId;

        owner = repository.initializeUser();
        isInterestedByUser = new MutableLiveData<>();
        isUserGoing = new MutableLiveData<>();

        mRepository.isUserInterestedEvent(eventId, new TaskResultListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                isInterestedByUser.setValue(result);
            }
        });


    }

    public Event getEvent() {
        event = mRepository.getEventFromLocalDb(eventId);
        mRepository.loadUserById(event.getOwnerId());
        return event;
    }

    public MutableLiveData<Boolean> getIsInterestedByUser() {
        return isInterestedByUser;
    }

    public void addEventToInterested() {
        mRepository.addEventToInterested(event, new TaskResultListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                isInterestedByUser.setValue(result);
            }
        });
    }

    public void removeFromInterested() {
        mRepository.removeFromInterested(eventId, new TaskResultListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                isInterestedByUser.setValue(!result);
            }
        });
    }

    public void addEventToGoing() {
        mRepository.addEventToGoing(event, new TaskResultListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                isUserGoing.setValue(result);
            }
        });
    }

    public void removeFromGoing() {
        mRepository.removeEventFromGoing(eventId, new TaskResultListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                isUserGoing.setValue(!result);
            }
        });
    }

    public MutableLiveData<Boolean> getIsUserGoing() {
        return isUserGoing;
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
