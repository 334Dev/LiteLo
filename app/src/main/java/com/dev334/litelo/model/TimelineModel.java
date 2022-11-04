package com.dev334.litelo.model;

import java.util.Map;

public class TimelineModel {
    private String name, parent, link, desc, date, time, id;

    public TimelineModel(String name, String parent, String link, String desc, String date, String time) {
        this.name = name;
        this.parent = parent;
        this.link = link;
        this.desc = desc;
        this.date = date;
        this.time = time;
    }

    public TimelineModel() {
    }

    public TimelineModel(Map<String, Object> map) {
        name = (String) map.get("name");
        parent = (String) map.get("parent");
        link = (String) map.get("link");
        desc = (String) map.get("desc");
        date = (String) map.get("date");
        time = (String) map.get("time");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}