package com.dev334.litelo;

public class clubModel {
    private String Topic;
    private String Timing;
    private String Description;


    private String Link;

    public clubModel(){
        //empty constructor
    }

    public clubModel(String topic, String timing, String description, String link) {
        Topic = topic;
        Timing = timing;
        Description = description;
        Link=link;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String timing) {
        Timing = timing;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
