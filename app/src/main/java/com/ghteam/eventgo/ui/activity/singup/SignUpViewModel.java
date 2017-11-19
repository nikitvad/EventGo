package com.ghteam.eventgo.ui.activity.singup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.ghteam.eventgo.data.entity.UserEntry;

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

    private UserEntry userData = new UserEntry();

    public UserEntry getUserData() {
        return userData;
    }

    public void setUserData(UserEntry userData) {
        this.userData = userData;
    }


    //    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }

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

//    public String getDisplayDate() {
//        return displayDate;
//    }
//
//    public void setDisplayDate(String displayDate) {
//        this.displayDate = displayDate;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }


    @Override
    public String toString() {
        return "SignUpViewModel{" +
                "isLoading=" + isLoading +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", copyOfPassword='" + copyOfPassword + '\'' +
                userData.toString()+
//                ", displayDate='" + displayDate + '\'' +
//                ", date='" + date + '\'' +
                '}';
    }
}
