package com.dev334.litelo.model;

public class EventRequest {
    private String deptEventId;

    public String getDeptEventId() {
        return deptEventId;
    }

    public void setDeptEventId(String deptEventId) {
        this.deptEventId = deptEventId;
    }

    public EventRequest() {
    }

    public EventRequest(String deptEventId) {
        this.deptEventId = deptEventId;
    }
}
