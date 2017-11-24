package com.ghteam.eventgo.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */

public class User {
    //    private String firebaseId;
    private String firstName = "";
    private String lastName = "";
    private Date birthday = new Date(0);
    private String description = "";
    private String profileImageUri = "";
    private List<Category> interests = new ArrayList<>();

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    public String getFirebaseId() {
//        return firebaseId;
//    }
//
//    public void setFirebaseId(String firebaseId) {
//        this.firebaseId = firebaseId;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public List<Category> getInterests() {
        return interests;
    }

    public void setInterests(List<Category> interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", description='" + description + '\'' +
                ", profileImageUri='" + profileImageUri + '\'' +
                ", interests=" + interests +
                '}';
    }
}
