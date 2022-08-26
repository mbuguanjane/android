package com.restrorant.myapplication.Model;

public class ReservationReq {
    String phone,name,date,time,people,status,price,location;

    public ReservationReq() {
    }

    public ReservationReq(String phone, String name, String date, String time, String people, String status, String price, String location) {
        this.phone = phone;
        this.name = name;
        this.date = date;
        this.time = time;
        this.people = people;
        this.status = status;
        this.price = price;
        this.location = location;
    }

    public String getPhone() {
        return phone;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
