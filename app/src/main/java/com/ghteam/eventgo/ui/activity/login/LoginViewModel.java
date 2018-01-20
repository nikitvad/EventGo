package com.ghteam.eventgo.ui.activity.login;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ghteam.eventgo.data_new.Repository;
import com.ghteam.eventgo.data_new.entity.User;
import com.ghteam.eventgo.data_new.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.data_new.task.TaskStatus;
import com.ghteam.eventgo.util.FacebookUserJsonConverter;
import com.ghteam.eventgo.util.LoginInResult;
import com.ghteam.eventgo.util.network.AccountStatus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by nikit on 17.11.2017.
 */

public class LoginViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<LoginInResult> loginInResult = new MutableLiveData<>();

    private MutableLiveData<User> currentUser;
    private MutableLiveData<TaskStatus> logInTaskStatus;


    private final static String TAG = LoginViewModel.class.getSimpleName();

    private LoginViewModel(final Repository repository) {
        this.mRepository = repository;


        logInTaskStatus = repository.getLogInTaskStatus();

        currentUser = repository.initializeCurrentUser();

        logInTaskStatus.observeForever(new Observer<TaskStatus>() {
            @Override
            public void onChanged(@Nullable TaskStatus taskStatus) {
                switch (taskStatus){
                    case SUCCESS:
                        repository.loadCurrentUser();
                }
            }
        });

//        accountStatus = repository.getCurrentAccountStatus();
    }


    void logInWithEmailAndPassword(String email, String password){
        mRepository.loginWithEmail(email, password);
    }

    void logInWithFacebook(Activity activity, CallbackManager callbackManager){
        mRepository.logInWithFacebook(activity, callbackManager);
    }

    MutableLiveData<TaskStatus> getLogInTaskStatus(){
        return logInTaskStatus;
    }

    MutableLiveData<User> getCurrentUser(){
        return currentUser;
    }

    void addNewFacebookUser(final String uid, AccessToken token,
                            @Nullable final FirebaseDatabaseManager.OnPullUserResultListener listener) {
        Bundle params = new Bundle();
        params.putString("fields", "first_name,last_name,picture");
        new GraphRequest(
                token,
                "/" + token.getUserId(),
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        User user = FacebookUserJsonConverter.getUser(response);
                        mRepository.updateUser(user, uid);
                    }
                }
        ).executeAsync();
    }

    public static class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Repository mRepository;

        public LoginViewModelFactory(Repository mRepository) {
            this.mRepository = mRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new LoginViewModel(mRepository);
        }
    }
}
