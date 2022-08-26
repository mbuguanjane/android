package com.rodikenya.rodiseriou.Model;

public class Intro {
   private  String Title,SubTitle,Description;
   private  boolean expandle;

    public Intro() {
    }

    public boolean isExpandle() {
        return expandle;
    }

    public void setExpandle(boolean expandle) {
        this.expandle = expandle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
