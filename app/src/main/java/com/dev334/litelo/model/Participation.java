
package com.dev334.litelo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Participation {

    @SerializedName("teamId")
    @Expose
    private Integer teamId;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("event")
    @Expose
    private Event event;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
