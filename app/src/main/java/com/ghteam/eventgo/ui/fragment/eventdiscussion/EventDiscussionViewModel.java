package com.ghteam.eventgo.ui.fragment.eventdiscussion;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.DiscussionMessage;
import com.ghteam.eventgo.data.task.TaskStatus;

import java.util.List;

/**
 * Created by nikit on 02.02.2018.
 */

public class EventDiscussionViewModel extends ViewModel {

    private Repository mRepository;
    private String mEventId;

    private MutableLiveData<List<DiscussionMessage>> messages;
    private MutableLiveData<TaskStatus> loadMessagesTaskStatus;

    private EventDiscussionViewModel(Repository repository, String eventId) {
        this.mRepository = repository;
        mEventId = eventId;

        messages = mRepository.initializeDiscussionMessages(eventId);
        loadMessagesTaskStatus = mRepository.getLoadDiscussionMessagesTaskStatus();
    }

    public void loadNextMessages(int limit) {
        mRepository.loadNextDiscussionMessages(limit);
    }

    public MutableLiveData<List<DiscussionMessage>> getMessages() {
        return messages;
    }

    public MutableLiveData<TaskStatus> getLoadMessagesTaskStatus() {
        return loadMessagesTaskStatus;
    }

    public static class EventDiscussionViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;
        private String mEventId;

        public EventDiscussionViewModelFactory(Repository repository, String eventId) {
            mRepository = repository;
            mEventId = eventId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EventDiscussionViewModel(mRepository, mEventId);
        }
    }
}
