package com.ghteam.eventgo.ui.activity.profilesettings;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.User;
import com.ghteam.eventgo.data.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.util.LiveDataList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikit on 20.11.2017.
 */

public class ProfileSettingsViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<String> mFirstName;
    private MutableLiveData<String> mLastName;
    private MutableLiveData<String> mImageUrl;
    private LiveDataList<Category> mCategoriesList;
    private MutableLiveData<User> mUser;
    private MutableLiveData<String> mUserDescription;

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<SaveUserResult> mSaveUserInfoResult;

    private static final String TAG = ProfileSettingsViewModel.class.getSimpleName();

    public ProfileSettingsViewModel(Repository repository) {
        mRepository = repository;

        isLoading = new MutableLiveData<>();
        mSaveUserInfoResult = new MutableLiveData<>();

        mLastName = new MutableLiveData<>();
        mFirstName = new MutableLiveData<>();
        mImageUrl = new MutableLiveData<>();

        mCategoriesList = new LiveDataList<>();
        mUserDescription = new MutableLiveData<>();

        mUser = repository.getCurrentAccount();

        mUser.observeForever(new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    Log.d(TAG, "onStatusChanged: " + user.toString());
                    mFirstName.setValue(user.getFirstName());
                    mLastName.setValue(user.getLastName());
                    mCategoriesList.setValue(user.getInterests());
                    mUserDescription.setValue(user.getDescription());
                    mImageUrl.setValue(user.getProfileImageUrl());
                }
            }
        });
    }

    LiveDataList<Category> getCategories() {
        return mCategoriesList;
    }

    void setCategories(List<Category> categories) {
        mUser.getValue().setInterests(new ArrayList<>(categories));
        mCategoriesList.setValue(categories);
    }

    public MutableLiveData<String> getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        if (firstName != null && firstName.length() > 0) {
            mUser.getValue().setFirstName(firstName);
        }
    }

    public MutableLiveData<String> getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        if (lastName != null && lastName.length() > 0) {
            mUser.getValue().setLastName(lastName);
        }
    }

    public MutableLiveData<String> getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String url) {
        mImageUrl.setValue(url);
    }

    public MutableLiveData<String> getUserDescription() {
        return mUserDescription;
    }

    public void setUserDescription(String description) {
        mUser.getValue().setDescription(description);
    }

    public void saveUserData() {

        isLoading.setValue(true);

        Log.d(TAG, "saveUserData: " + mUser.getValue().toString());

        mRepository.pushUser(FirebaseAuth.getInstance().getUid(),
                mUser.getValue(),
                new FirebaseDatabaseManager.OnPullUserResultListener() {
                    @Override
                    public void onSuccess() {
                        isLoading.setValue(false);
                        mSaveUserInfoResult.setValue(SaveUserResult.RESULT_OK);
                    }

                    @Override
                    public void onFail(Exception e) {
                        isLoading.setValue(false);
                        mSaveUserInfoResult.setValue(SaveUserResult.RESULT_FAIL);
                    }
                });

    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<SaveUserResult> getSaveUserResult() {
        return mSaveUserInfoResult;
    }

    public static class ProfileSettingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private final Repository mRepository;


        ProfileSettingViewModelFactory(Repository repository) {
            mRepository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProfileSettingsViewModel(mRepository);
        }
    }

    public enum SaveUserResult {
        RESULT_NONE,
        RESULT_OK,
        RESULT_FAIL
    }

}
