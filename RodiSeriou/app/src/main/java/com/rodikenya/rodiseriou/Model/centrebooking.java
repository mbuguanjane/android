package com.rodikenya.rodiseriou.Model;

public class centrebooking {
    String BookingId,EventDetails,Phone,BookingDate,Date,Provisions;
    int BookingStatus;

    public centrebooking() {
    }

    public String getProvisions() {
        return Provisions;
    }

    public void setProvisions(String provisions) {
        Provisions = provisions;
    }

    public int getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(int bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getEventDetails() {
        return EventDetails;
    }

    public void setEventDetails(String eventDetails) {
        EventDetails = eventDetails;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
