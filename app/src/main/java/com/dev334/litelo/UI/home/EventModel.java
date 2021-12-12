package com.dev334.litelo.UI.home;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Map;

public class EventModel {
    private String Name, Parent, Link, Desc, Date, Time;
    private Map<String, String> Coordinator;

    public EventModel(Map<String, Object> map) {
        Name= (String) map.get("Name");
        Parent= (String) map.get("Parent");
        Link= (String) map.get("Link");
        Desc= (String) map.get("Desc");
        Date= (String) map.get("Date");
        Time= (String) map.get("Time");
        Coordinator= (Map<String, String>) map.get("Coordinator");
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate(){
        return Date;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setCoordinator(Map<String, String> coordinator) {
        Coordinator = coordinator;
    }

    public String getName() {
        return Name;
    }

    public String getParent() {
        return Parent;
    }

    public String getLink() {
        return Link;
    }

    public Map<String, String> getCoordinator() {
        return Coordinator;
    }
}
