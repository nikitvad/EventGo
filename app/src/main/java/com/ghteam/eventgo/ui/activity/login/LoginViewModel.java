//package com.ghteam.eventgo.ui.activity.login;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.MediatorLiveData;
//import android.arch.lifecycle.MutableLiveData;
//import android.arch.lifecycle.ViewModel;
//import android.util.Log;
//import android.util.MutableBoolean;
//
//import com.ghteam.eventgo.util.firebase.FirebaseLoginManager;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
///**
// * Created by nikit on 17.11.2017.
// */
//
//public class LoginViewModel extends ViewModel {
//
//    private final static String TAG = LoginViewModel.class.getSimpleName();
//
//    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
//
//
//    public void loginWithEmail(String email, String password) {
//        //TODO: handle login states
//
//        isLoading.setValue(true);
//        FirebaseLoginManager.loginWithEmail(email, password, new FirebaseLoginManager.OnLoginResultListener() {
//            @Override
//            public void onSuccess(FirebaseUser firebaseUser) {
//                Log.d(TAG, "onSuccess: " + firebaseUser.getDisplayName());
//                isLoading.setValue(false);
//            }
//
//            @Override
//            public void onFailed(Exception e) {
//                Log.d(TAG, "onFailed: " + e.getMessage());
//                isLoading.setValue(false);
//            }
//        });
//
//    }
//
//
//    public void loginWithGoogle(){
//
//    }
//
//
//    public LiveData<Boolean> getIsLoading() {
//        return isLoading;
//    }
//}
