package com.rodikenya.rodiseriou.Model;

public class TrainingItems {
    String TrainingID,Name,Price,Description,TrainingMenuID,Link,Availability;

    public TrainingItems() {
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getTrainingID() {
        return TrainingID;
    }

    public void setTrainingID(String trainingID) {
        TrainingID = trainingID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTrainingMenuID() {
        return TrainingMenuID;
    }

    public void setTrainingMenuID(String trainingMenuID) {
        TrainingMenuID = trainingMenuID;
    }
}
