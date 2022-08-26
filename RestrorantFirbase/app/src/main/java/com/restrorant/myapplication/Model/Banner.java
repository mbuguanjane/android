package com.restrorant.myapplication.Model;

public class Banner {
    private String  bannerid,Name,Link;

    public Banner() {
    }

    public Banner(String bannerid, String name, String link) {
        this.bannerid = bannerid;
        Name = name;
        Link = link;
    }

    public String getBannerid() {
        return bannerid;
    }

    public void setBannerid(String bannerid) {
        this.bannerid = bannerid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
