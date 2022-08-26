package com.rodikenya.rodiseriou.Model;

public class CentreModel {
     String EventID,EventName,EventPrice,Available,Link,Provision,Options;

    public CentreModel() {
    }

    public String getProvision() {
        return Provision;
    }

    public void setProvision(String provision) {
        Provision = provision;
    }

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventPrice() {
        return EventPrice;
    }

    public void setEventPrice(String eventPrice) {
        EventPrice = eventPrice;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
