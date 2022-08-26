package com.rodikenya.rodiseriou.RoomDatabase.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "Cart",indices = {@Index(value = {"name"},
        unique = true)})

public class Cart {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "Link")
    private String link;

    @ColumnInfo(name = "status")
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @ColumnInfo(name = "Quantity")
    private int quantity;

    @ColumnInfo(name = "Available")
    private int available;

    @ColumnInfo(name = "productid")
    private int productid;

    public Cart() {
    }
    @Ignore
    public Cart(String name, float price, String description, String link, int quantity, int available, int productid) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.link = link;
        this.quantity = quantity;
        this.available = available;
        this.productid = productid;
        this.status=true;
    }




    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder(name).append("\n").append(price).append("\n").append(quantity).append("\n").append(description).append("\n").append(link).toString();
    }
}
