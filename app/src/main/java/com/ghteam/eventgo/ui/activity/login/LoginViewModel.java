package com.ghteam.eventgo.ui.activity.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ghteam.eventgo.data.Repository;
import com.ghteam.eventgo.data.model.User;
import com.ghteam.eventgo.data.network.FirebaseDatabaseManager;
import com.ghteam.eventgo.util.FacebookUserJsonConverter;
import com.ghteam.eventgo.util.LoginInResult;
import com.ghteam.eventgo.util.network.AccountStatus;

/**
 * Created by nikit on 17.11.2017.
 */

public class LoginViewModel extends ViewModel {
    private Repository mRepository;

    private MutableLiveData<AccountStatus> accountStatus;
    private MutableLiveData<LoginInResult> loginInResult = new MutableLiveData<>();

    private final static String TAG = LoginViewModel.class.getSimpleName();

    private LoginViewModel(Repository repository) {
        this.mRepository = repository;
        accountStatus = repository.getCurrentAccountStatus();

    }

    MutableLiveData<AccountStatus> getAccountStatus() {
        return accountStatus;
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
                        mRepository.pushUser(uid, user, listener);
                    }
                }
        ).executeAsync();
    }

    MutableLiveData<LoginInResult> getLoginInResult() {
        return loginInResult;
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
