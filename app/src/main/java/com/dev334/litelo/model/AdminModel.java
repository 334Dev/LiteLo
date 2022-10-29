package com.dev334.litelo.model;

import androidx.annotation.NonNull;

public class AdminModel {
    private String event, eventId, dept, deptId;


    public AdminModel() {

    }

    public AdminModel(String event, String eventId, String dept, String deptId) {
        this.event = event;
        this.eventId = eventId;
        this.dept = dept;
        this.deptId = deptId;
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @NonNull
    @Override
    public String toString() {
        return event;
    }
}
