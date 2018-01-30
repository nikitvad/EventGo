package com.ghteam.eventgo.data.entity;

import io.realm.RealmObject;

/**
 * Created by nikit on 20.11.2017.
 */

public class Category extends RealmObject {

//    @PrimaryKey
//    public int id;

    private String name;
    private String icon;

    public Category() {
    }

    public Category(String name, String iconUrl) {
        this.name = name;
        this.icon = iconUrl;
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
        return "Category{" +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        return icon != null ? icon.equals(category.icon) : category.icon == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }
}
