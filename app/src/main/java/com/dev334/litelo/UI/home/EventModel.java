package com.dev334.litelo.UI.home;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Map;

public class EventModel {
    private String Name, Parent, Link, Team, Date;
    private Boolean Online;
    private Map<String, String> Coordinator;

    public EventModel(Map<String, Object> map) {
        Name= (String) map.get("Name");
        Parent= (String) map.get("Parent");
        Link= (String) map.get("Link");
        Team= (String) map.get("Team");
        Online= (Boolean) map.get("Online");
        Date= (String) map.get("Date");
        Coordinator= (Map<String, String>) map.get("Coordinator");
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

    public void setTeam(String team) {
        Team = team;
    }

    public void setOnline(Boolean online) {
        Online = online;
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

    public String getTeam() {
        return Team;
    }

    public Boolean getOnline() {
        return Online;
    }

    public Map<String, String> getCoordinator() {
        return Coordinator;
    }
}
