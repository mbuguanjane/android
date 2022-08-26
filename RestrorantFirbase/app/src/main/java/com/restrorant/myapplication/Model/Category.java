package com.restrorant.myapplication.Model;

public class Category {
    String name,image,restorauntID;

    public Category() {
    }

    public Category(String name, String image, String restorauntID) {
        this.name = name;
        this.image = image;
        this.restorauntID = restorauntID;
    }

    public String getRestorauntID() {
        return restorauntID;
    }

    public void setRestorauntID(String restorauntID) {
        this.restorauntID = restorauntID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
