package com.ghteam.eventgo.data.entity;

import java.util.List;

/**
 * Created by nikit on 30.11.2017.
 */

public class Event {
    private String ownerId;
    private String name;
    private String description;
    private String address;
    private List<Category> categories;
    private List<String> images;
    private Location location;


    public Event() {
    }

    public Event(String name, String description, String address, List<Category> categories, List<String> images) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.categories = categories;
        this.images = images;
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

    public List<Category> getCategories() {
        return categories;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
