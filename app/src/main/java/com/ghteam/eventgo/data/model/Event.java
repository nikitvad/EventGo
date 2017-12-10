package com.ghteam.eventgo.data.model;

import java.util.List;

/**
 * Created by nikit on 30.11.2017.
 */

public class Event {
    private String ownerId;
    private String name;
    private String description;
    private String address;
    private Category category;
    private List<String> images;
    private Location location;

    public Event() {
    }

    public Event(String name, String description, String address, Category category, List<String> images) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
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

    public void setImages(List<String> images) {
        this.images = images;
    }
}
