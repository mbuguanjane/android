package com.rodikenya.rodiseriou.Model;

public class TrainingOrderModel {
    String TraineeId,Phone,TrainingDetails,Members,Address,Name;
    int AppStatus;

    public TrainingOrderModel() {
    }

    public String getTraineeId() {
        return TraineeId;
    }

    public void setTraineeId(String traineeId) {
        TraineeId = traineeId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTrainingDetails() {
        return TrainingDetails;
    }

    public void setTrainingDetails(String trainingDetails) {
        TrainingDetails = trainingDetails;
    }

    public String getMembers() {
        return Members;
    }

    public void setMembers(String members) {
        Members = members;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAppStatus() {
        return AppStatus;
    }

    public void setAppStatus(int appStatus) {
        AppStatus = appStatus;
    }
}
