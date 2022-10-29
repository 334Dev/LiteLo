package com.dev334.litelo.model;

public class NotificationModel {
    private String title, body, eventId;
    private Long time;

    public NotificationModel() {
    }

    public NotificationModel(String eventId, String title, String body, Long time) {
        this.title = title;
        this.body = body;
        this.time = time;
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}