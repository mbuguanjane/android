package com.restrorant.myapplication.Model;

import com.restrorant.myapplication.RoomDatab.Model.CartModel;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String Status;
    private List<CartModel> foods;

    public Request() {
    }

    public Request(String phone, String name, String address, String total,String Status, List<CartModel> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.Status=Status;
        this.foods = foods;

    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<CartModel> getFoods() {
        return foods;
    }

    public void setFoods(List<CartModel> foods) {
        this.foods = foods;
    }
}
