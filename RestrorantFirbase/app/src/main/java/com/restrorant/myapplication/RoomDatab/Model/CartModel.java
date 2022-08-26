package com.restrorant.myapplication.RoomDatab.Model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Cart")
public class CartModel   {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;

    @ColumnInfo(name="productId")
    private String productId;

    @ColumnInfo(name="ProductName")
    private String ProductName;

    @ColumnInfo(name="Quantity")
    private String Quantity;

    @ColumnInfo(name="Price")
    private String Price;
    @ColumnInfo(name="Image")
    private String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public CartModel() {
    }

    public CartModel(String productId, String productName, String quantity, String price, String image) {
        this.productId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }


}
