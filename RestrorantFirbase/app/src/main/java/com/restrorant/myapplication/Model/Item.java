package com.restrorant.myapplication.Model;

import java.util.List;

public class Item {
    private List<Itemm> itemms;

    public Item() {
    }

    public Item(List<Itemm> itemms) {
        this.itemms = itemms;
    }

    public List<Itemm> getItemms() {
        return itemms;
    }

    public void setItemms(List<Itemm> itemms) {
        this.itemms = itemms;
    }
}
