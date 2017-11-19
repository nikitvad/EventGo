package com.ghteam.eventgo.data.entity;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by nikit on 19.11.2017.
 */

public class UserEntry {
//    private String firebaseId;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String profileImageUri;
    private String[] interests;

    public UserEntry() {
    }

    public UserEntry(String firstName, String lastName) {
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

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String[] getInterests() {
        return interests;
    }

    public void setInterests(String[] interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return "UserEntry{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", profileImageUri='" + profileImageUri + '\'' +
                ", interests=" + Arrays.toString(interests) +
                '}';
    }
}
