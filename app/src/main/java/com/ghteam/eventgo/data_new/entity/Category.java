package com.ghteam.eventgo.data_new.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by nikit on 20.11.2017.
 */
@Entity(tableName = "categories", foreignKeys = @ForeignKey(
        entity = Event.class,
        parentColumns = "id",
        childColumns = "ownerId"
))
public class Category {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String ownerId;

    private String name;
    private String icon;

    public Category() {
    }

    public Category(String name, String iconUrl) {
        this.name = name;
        this.icon = iconUrl;
    }

    public Category(String ownerId, String name, String iconUrl) {
        this.ownerId = ownerId;
        this.name = name;
        this.icon = iconUrl;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (ownerId != null ? !ownerId.equals(category.ownerId) : category.ownerId != null)
            return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        return icon != null ? icon.equals(category.icon) : category.icon == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }
}
