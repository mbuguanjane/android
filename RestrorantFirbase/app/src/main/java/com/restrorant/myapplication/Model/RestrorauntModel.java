package com.restrorant.myapplication.Model;

public class RestrorauntModel {
    String name,image,description;
    Long price;

    public RestrorauntModel() {
    }

    public RestrorauntModel(String name, String image, String description, Long price) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
