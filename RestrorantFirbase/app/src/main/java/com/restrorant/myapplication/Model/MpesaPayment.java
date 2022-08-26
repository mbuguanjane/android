package com.restrorant.myapplication.Model;

public class MpesaPayment {
    private   Body body;

    public MpesaPayment() {
    }

    public MpesaPayment(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
