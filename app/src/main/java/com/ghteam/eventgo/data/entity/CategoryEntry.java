package com.ghteam.eventgo.data.entity;

import java.util.ArrayList;

/**
 * Created by nikit on 20.11.2017.
 */

public class CategoryEntry {
    private String id;
    private String name;
    private String icon;

    public CategoryEntry() {
    }

    public CategoryEntry(String id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.icon = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "CategoryEntry{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
