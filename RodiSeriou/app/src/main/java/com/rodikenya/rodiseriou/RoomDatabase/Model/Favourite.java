package com.rodikenya.rodiseriou.RoomDatabase.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favourite",indices = {@Index(value = {"name"},
        unique = true)})
public class Favourite {



    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public  String name;

    @ColumnInfo(name = "link")
    public  String link;

    @ColumnInfo(name = "price")
    public  String price;

    @ColumnInfo(name = "menuId")
    public  String menuId;


    @Ignore
    public Favourite(String name, String link, String price, String menuId) {
        this.name = name;
        this.link = link;
        this.price = price;
        this.menuId = menuId;
    }

    public Favourite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder(name).append("\n").append(price).append("\n").append(link).toString();

    }
}
