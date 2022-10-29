package com.dev334.litelo.UI.notification;

public class NotificationModel {
    private String name, desc, time;

    public NotificationModel(String name, String desc, String time) {
        this.name = name;
        this.desc = desc;
        this.time = time;
    }

    public NotificationModel(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
