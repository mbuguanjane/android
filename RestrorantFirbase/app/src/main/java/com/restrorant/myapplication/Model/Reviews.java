package com.restrorant.myapplication.Model;

public class Reviews {
    String comments,ratings,restourantID,phone;

    public Reviews() {
    }

    public Reviews(String comments, String ratings, String restourantID, String phone) {
        this.comments = comments;
        this.ratings = ratings;
        this.restourantID = restourantID;
        this.phone = phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRestourantID() {
        return restourantID;
    }

    public void setRestourantID(String restourantID) {
        this.restourantID = restourantID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
