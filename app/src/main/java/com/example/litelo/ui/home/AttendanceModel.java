package com.example.litelo.ui.home;

public class AttendanceModel {

    private Double Absent;
    private Double Present;
    private Boolean presentStatus, absentStatus;
    private String Subject;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    public Boolean getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(Boolean presentStatus) {
        this.presentStatus = presentStatus;
    }

    public Boolean getAbsentStatus() {
        return absentStatus;
    }

    public void setAbsentStatus(Boolean absentStatus) {
        this.absentStatus = absentStatus;
    }

    public AttendanceModel(){

    }

    public Double getAbsent() {
        return Absent;
    }

    public void setAbsent(Double absent) {
        Absent = absent;
    }

    public Double getPresent() {
        return Present;
    }

    public void setPresent(Double present) {
        Present = present;
    }

}
