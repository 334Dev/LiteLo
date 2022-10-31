
package com.dev334.litelo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoordinatorResponse {

    @SerializedName("eventCoordies")
    @Expose
    private List<EventCoordinator> eventCoordies = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<EventCoordinator> getEventCoordies() {
        return eventCoordies;
    }

    public void setEventCoordies(List<EventCoordinator> eventCoordies) {
        this.eventCoordies = eventCoordies;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
