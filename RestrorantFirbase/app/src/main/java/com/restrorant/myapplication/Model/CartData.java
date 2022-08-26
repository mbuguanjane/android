package com.restrorant.myapplication.Model;

public class CartData {
    private  String productId, ProductName,  Quantity,  Price;
    private int id;

    public CartData() {
    }

    public CartData(String productId, String productName, String quantity, String price, int id) {
        this.productId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
