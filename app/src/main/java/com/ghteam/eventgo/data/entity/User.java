package com.ghteam.eventgo.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 19.11.2017.
 */
@Entity(tableName = "users", foreignKeys = @ForeignKey(
        entity = Event.class,
        parentColumns = "id",
        childColumns = "ownerId"
))
public class User {
    @NonNull
    @PrimaryKey()
    private String id;

    public int ownerId;

    private String firstName = "";
    private String lastName = "";
    private Date birthday = new Date(0);
    private String description = "";
    private String profileImageUrl = "";
    private List<Category> interests = new ArrayList<>();

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", interests=" + interests.toString() +
                '}';
    }
}
