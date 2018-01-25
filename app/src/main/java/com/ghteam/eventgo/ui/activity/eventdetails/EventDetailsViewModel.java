package com.ghteam.eventgo.ui.activity.eventdetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.database.ImageEntry;
import com.ghteam.eventgo.data_new.entity.Category;
import com.ghteam.eventgo.data_new.entity.Event;
import com.ghteam.eventgo.data_new.entity.Location;
import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.util.network.OnTaskStatusChangeListener;

import java.util.List;

/**
 * Created by nikit on 13.12.2017.
 */

public class EventDetailsViewModel extends ViewModel {
    private Repository mRepository;

    public LiveData<Event> event;
    public LiveData<Category> category;
    public LiveData<List<ImageEntry>> images;
    public LiveData<Location> location;
    public LiveData<User> user;


    private EventDetailsViewModel(Repository repository, final String eventId) {
        mRepository = repository;

//        event = mRepository.getEventById(eventId);
//        category = mRepository.getCategoryByOwner(eventId);
//        images = mRepository.getImagesByOwner(eventId);
//        location = mRepository.getLocationByOwner(eventId);
//        user = mRepository.initializeUser();
//
//        event.observeForever(new Observer<Event>() {
//            @Override
//            public void onChanged(@Nullable Event event) {
//
//                Log.d("fsddfsfd", "onChanged: " + event.getOwnerId());
//                mRepository.loadUserById(event.getOwnerId(), new OnTaskStatusChangeListener() {
//                    @Override
//                    public void onStatusChanged(TaskStatus status) {
////                        TODO: display progress bar
//                    }
//                });
//            }
//        });

    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public void setEvent(LiveData<Event> event) {
        this.event = event;
    }

    public LiveData<Category> getCategory() {
        return category;
    }

    public void setCategory(LiveData<Category> category) {
        this.category = category;
    }

    public LiveData<List<ImageEntry>> getImages() {
        return images;
    }

    public void setImages(LiveData<List<ImageEntry>> images) {
        this.images = images;
    }

    public LiveData<Location> getLocation() {
        return location;
    }

    public void setLocation(LiveData<Location> location) {
        this.location = location;
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
