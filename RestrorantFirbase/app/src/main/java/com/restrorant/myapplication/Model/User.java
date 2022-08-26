package com.restrorant.myapplication.Model;

import android.widget.EditText;

public class User {

    String FirstName,SecondName,LastName,Phone,Email,UserLevel,Password;

    public User(String firstName, String secondName, String lastName, String phone, String email, String userLevel, String password) {
        FirstName = firstName;
        SecondName = secondName;
        LastName = lastName;
        Phone = phone;
        Email = email;
        UserLevel = userLevel;
        Password = password;
    }

    public User() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
