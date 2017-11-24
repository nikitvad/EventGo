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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nikit on 20.11.2017.
 */

public class ProfileSettingsViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<String> mFirstName;
    private MutableLiveData<String> mLastName;
    private MutableLiveData<Set<Category>> mCategoriesList;
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

        mCategoriesList = new MutableLiveData<>();
        mUserDescription = new MutableLiveData<>();


        mUser = repository.getCurrentUser();

        mUser.observeForever(new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mFirstName.setValue(user.getFirstName());
                mLastName.setValue(user.getLastName());
                mCategoriesList.setValue(new HashSet<>(user.getInterests()));
                mUserDescription.setValue(user.getDescription());
            }
        });
    }

    MutableLiveData<Set<Category>> getCategories() {
        return mCategoriesList;
    }

    void setCategories(Set<Category> categories) {
        mUser.getValue().setInterests(new ArrayList<Category>(categories));
        mCategoriesList.setValue(categories);
    }

    MutableLiveData<String> getFirstName() {
        return mFirstName;
    }

    void setFirstName(String firstName) {
        if (firstName != null && firstName.length() > 0) {
            mFirstName.setValue(firstName);
            mUser.getValue().setFirstName(firstName);
        }
    }

    MutableLiveData<String> getLastName() {
        return mLastName;
    }

    void setLastName(String lastName) {
        if (lastName != null && lastName.length() > 0) {
            mLastName.setValue(lastName);
            mUser.getValue().setLastName(lastName);
        }
    }

    MutableLiveData<String> getUserDescription() {
        return mUserDescription;
    }

    void setUserDescription(String description) {
        mUserDescription.setValue(description);
        mUser.getValue().setDescription(description);
    }

    public void saveUser() {

        isLoading.setValue(true);

        Log.d(TAG, "saveUser: " + mUser.getValue().toString());

        mRepository.pullUser(FirebaseAuth.getInstance().getUid(),
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
