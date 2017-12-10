package com.ghteam.eventgo.ui.activity.singup;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ghteam.eventgo.data.model.User;

/**
 * Created by nikit on 18.11.2017.
 */

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    //    private String firstName;
//    private String lastName;
    private String email;
    private String password;
    private String copyOfPassword;
//    private String displayDate;
//    private String date;

    private User userData = new User();

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCopyOfPassword() {
        return copyOfPassword;
    }

    public void setCopyOfPassword(String copyOfPassword) {
        this.copyOfPassword = copyOfPassword;
    }

    @Override
    public String toString() {
        return "SignUpViewModel{" +
                "isLoading=" + isLoading +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", copyOfPassword='" + copyOfPassword + '\'' +
                userData.toString() +
//                ", displayDate='" + displayDate + '\'' +
//                ", date='" + date + '\'' +
                '}';
    }
}
