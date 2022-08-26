package com.rodikenya.rodiseriou.Model;

public class MediaObject {

    private String TutorialID;
    private String TutorialName;
    private String Link;
    private String Likes;
    private String Dislike;


    public MediaObject() {
    }

    public String getTutorialID() {
        return TutorialID;
    }

    public void setTutorialID(String tutorialID) {
        TutorialID = tutorialID;
    }

    public String getTutorialName() {
        return TutorialName;
    }

    public void setTutorialName(String tutorialName) {
        TutorialName = tutorialName;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getDislike() {
        return Dislike;
    }

    public void setDislike(String dislike) {
        Dislike = dislike;
    }
}
