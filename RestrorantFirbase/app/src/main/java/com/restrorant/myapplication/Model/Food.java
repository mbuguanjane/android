package com.restrorant.myapplication.Model;

public class Food {
    private String menuId,price,name,image,discount,description;

    public Food() {
    }

    public Food(String menuId, String price, String name, String image, String discount, String description) {
        this.menuId = menuId;
        this.price = price;
        this.name = name;
        this.image = image;
        this.discount = discount;
        this.description = description;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
