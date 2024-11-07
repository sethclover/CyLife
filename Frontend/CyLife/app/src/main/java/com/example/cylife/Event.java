package com.example.cylife;

public class Event {
    private String eventName;
    private String date;
    private String description;
    private String location;

    public Event(String eventName, String date, String description, String location) {
        this.eventName = eventName;
        this.date = date;
        this.description = description;
        this.location = location;
    }

    // Getters
    public String getEventName() {
        return eventName;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {return location;}

    public String getDescription() {
        return description;
    }
}

