package com.restrorant.myapplication.Model;

public class CallbackMetadata {
    private Item items;

    public CallbackMetadata() {
    }

    public CallbackMetadata(Item items) {
        this.items = items;
    }

    public Item getItems() {
        return items;
    }

    public void setItems(Item items) {
        this.items = items;
    }
}
