package com.dev334.litelo.model;

public class CoordinatorRequest {
    private String eventId;

    public CoordinatorRequest() {
    }

    public CoordinatorRequest(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
