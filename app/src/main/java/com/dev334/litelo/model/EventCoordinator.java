
package com.dev334.litelo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventCoordinator {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("user")
    @Expose
    private UserDetails user;

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

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

}
