package com.restrorant.myapplication.Model;

public class Body {
    private stkCallback stkCallback;

    public Body() {
    }

    public Body(com.restrorant.myapplication.Model.stkCallback stkCallback) {
        this.stkCallback = stkCallback;
    }

    public com.restrorant.myapplication.Model.stkCallback getStkCallback() {
        return stkCallback;
    }

    public void setStkCallback(com.restrorant.myapplication.Model.stkCallback stkCallback) {
        this.stkCallback = stkCallback;
    }
}
