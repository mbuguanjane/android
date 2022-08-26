package com.rodikenya.rodiseriou.Model;

public class User {
    private  String Phone,name,Birthdate,Address,error_msg,AvatarLink;
    public User() {
    }

    public String getAvatarLink() {
        return AvatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        AvatarLink = avatarLink;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
