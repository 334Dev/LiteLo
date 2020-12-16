package com.dev334.litelo;

public class clubModel {
    private String Topic, Timing, Description;

    public clubModel(){
        //empty constructor
    }

    public clubModel(String topic, String timing, String description) {
        Topic = topic;
        Timing = timing;
        Description = description;
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
