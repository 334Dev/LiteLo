package com.dev334.litelo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinator {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("event")
    @Expose
    private EventModel event;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

}