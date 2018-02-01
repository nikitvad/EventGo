package com.ghteam.eventgo.data.entity;


import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nikit on 30.11.2017.
 */
public class Event extends RealmObject {

    @PrimaryKey
    private String id;

    private String ownerId;

    private String name;

    private String description;

    private Date date;

    private String address;

    private Category category;

    //to resolve List type conflict between firebase firestore and realm database
    @Exclude
    private RealmList<String> realmImages;

    //to resolve List type conflict between firebase firestore and realm database
    @Ignore
    private List<String> images;

    private Location location;

    public Event() {
    }

    public Event(String name, String description, String address, Category category, RealmList<String> images) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Category getCategory() {
        return category;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public RealmList<String> getRealmImages() {
        return realmImages;
    }

    public void setRealmImages(RealmList<String> realmImages) {
        this.realmImages = realmImages;
    }

    public void setImages(List<String> images) {
        this.images = images;
        RealmList<String> realmList = new RealmList<>();
        realmList.addAll(images);
        realmImages = realmList;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", category=" + category +
                ", realmImages=" + realmImages +
                ", images=" + images +
                ", location=" + location +
                '}';
    }
}
