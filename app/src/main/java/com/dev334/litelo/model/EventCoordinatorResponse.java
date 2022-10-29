package com.dev334.litelo.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventCoordinatorResponse {

    @SerializedName("coordieEvent")
    @Expose
    private List<Coordinator> coordinatorList = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<Coordinator> getCoordieEvent() {
        return coordinatorList;
    }

    public void setCoordieEvent(List<Coordinator> coordinatorList) {
        this.coordinatorList = coordinatorList;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}