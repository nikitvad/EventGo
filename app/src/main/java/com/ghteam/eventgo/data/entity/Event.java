package com.ghteam.eventgo.data.entity;


import android.support.annotation.Nullable;

import com.ghteam.eventgo.util.network.LocationUtil;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String ownerName;

    private String ownerProfilePicture;

    private String name;

    private String description;

    private Date date;

    private String address;

    private Category category;

    private String discussionId;

    private boolean isDiscussionEnabled = false;

    @Ignore
    private long serializedLocation;

    private int interestedCount = 0;

    private int goingCount = 0;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerProfilePicture() {
        return ownerProfilePicture;
    }

    public void setOwnerProfilePicture(String ownerProfilePicture) {
        this.ownerProfilePicture = ownerProfilePicture;
    }

    public int getInterestedCount() {
        return interestedCount;
    }

    public void setInterestedCount(int interestedCount) {
        this.interestedCount = interestedCount;
    }

    public int getGoingCount() {
        return goingCount;
    }

    public void setGoingCount(int goingCount) {
        this.goingCount = goingCount;
    }

    @Nullable
    public String getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(String discussionId) {
        if (isDiscussionEnabled) {
            this.discussionId = discussionId;
        }
    }

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

    public boolean isDiscussionEnabled() {
        return isDiscussionEnabled;
    }

    public void setDiscussionEnabled(boolean discussionEnabled) {
        isDiscussionEnabled = discussionEnabled;
    }

    public double getSerializedLocation() {
        return serializedLocation;
    }

    public void setSerializedLocation(long serializedLocation) {
        this.serializedLocation = serializedLocation;
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

    @Exclude
    public void setRealmImages(RealmList<String> realmImages) {
        this.realmImages = realmImages;
    }

    public void setImages(List<String> images) {
        if (images != null && images.size() > 0) {
            this.images = images;
            RealmList<String> realmList = new RealmList<>();
            realmList.addAll(images);
            realmImages = realmList;
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerProfilePicture='" + ownerProfilePicture + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", category=" + category +
                ", discussionId='" + discussionId + '\'' +
                ", isDiscussionEnabled=" + isDiscussionEnabled +
                ", interestedCount=" + interestedCount +
                ", goingCount=" + goingCount +
                ", realmImages=" + realmImages +
                ", images=" + images +
                ", location=" + location +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();

        if (location != null) {
            serializedLocation = LocationUtil.serializeLatLong(location.getLatitude(), location.getLongitude());
        }

        result.put("id", id);
        result.put("ownerId", ownerId);
        result.put("ownerName", ownerName);
        result.put("ownerProfilePicture", ownerProfilePicture);
        result.put("name", name);
        result.put("description", description);
        result.put("date", date);
        result.put("address", address);
        result.put("category", category.toMap());
        result.put("discussionId", discussionId);
        result.put("isDiscussionEnabled", isDiscussionEnabled);
        result.put("interestedCount", interestedCount);
        result.put("goingCount", goingCount);
        result.put("images", images);
        result.put("serializedLocation", serializedLocation);

        if (location != null) {
            result.put("location", location.toMap());
        }
        return result;
    }


}
