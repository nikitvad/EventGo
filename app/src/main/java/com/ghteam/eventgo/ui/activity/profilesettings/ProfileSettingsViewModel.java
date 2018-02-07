package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.task.TaskStatus;
import com.ghteam.eventgo.util.LiveDataList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class ProfileSettingsViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<String> firstName;
    private MutableLiveData<String> lastName;
    private MutableLiveData<String> imageUrl;
    private LiveDataList<Category> categoriesList;
    private MutableLiveData<String> userDescription;

    private MutableLiveData<TaskStatus> updateUserTaskStatus;

    private MutableLiveData<User> currentUser;
    private MutableLiveData<TaskStatus> loadCurrentUserTaskStatus;

    private static final String TAG = ProfileSettingsViewModel.class.getSimpleName();

    public ProfileSettingsViewModel(Repository repository) {
        mRepository = repository;

        //        saveUserInfoResult = new MutableLiveData<>();


        lastName = new MutableLiveData<>();
        firstName = new MutableLiveData<>();
        imageUrl = new MutableLiveData<>();

        categoriesList = new LiveDataList<>();
        userDescription = new MutableLiveData<>();

        updateUserTaskStatus = repository.getUpdateUserTaskStatus();
        currentUser = repository.initializeCurrentUser();
        loadCurrentUserTaskStatus = repository.getLoadCurrentUserTaskStatus();

    }

    public void loadCurrentUser() {
        mRepository.loadCurrentUser();
    }

    MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    MutableLiveData<TaskStatus> getLoadCurrentUserTaskStatus() {
        return loadCurrentUserTaskStatus;
    }

    LiveDataList<Category> getCategories() {
        return categoriesList;
    }

    void setCategories(List<Category> categories) {
        currentUser.getValue().setInterests(new ArrayList<>(categories));
        categoriesList.setValue(categories);
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName != null && firstName.length() > 0) {
            currentUser.getValue().setFirstName(firstName);
        }
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName != null && lastName.length() > 0) {
            currentUser.getValue().setLastName(lastName);
        }
    }

    public MutableLiveData<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url) {
        imageUrl.setValue(url);
    }

    public MutableLiveData<String> getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String description) {
        currentUser.getValue().setDescription(description);
    }

    public void updateUser() {
        mRepository.updateUser(currentUser.getValue(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public MutableLiveData<TaskStatus> getUpdateUserTaskStatus() {
        return updateUserTaskStatus;
    }

    public static class ProfileSettingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private final Repository mRepository;


        public ProfileSettingViewModelFactory(Repository repository) {
            mRepository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProfileSettingsViewModel(mRepository);
        }
    }
}
