package com.example.my;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventTitle;
    private String eventDate;

    private long id;
    private String eventLocation;

    public Event(String eventTitle, String eventDate, String eventLocation) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
    }

    public String getEventTitle() {
        return eventTitle;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public long getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

