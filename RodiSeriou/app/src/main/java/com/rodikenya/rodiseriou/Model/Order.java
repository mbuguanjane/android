package com.rodikenya.rodiseriou.Model;

public class Order {
     private long OrderId;
     private int OrderStatus;
     private float OrderPrice;
     private String OrderDetail,OrderComment,OrderAddress,UserPhone,OrderDate,PaymentMethod;
     private Double Lat,Lng;
    public Order() {
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public long getOrderId() {
        return OrderId;
    }

    public void setOrderId(long orderId) {
        OrderId = orderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
