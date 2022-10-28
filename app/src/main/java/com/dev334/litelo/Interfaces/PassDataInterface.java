package com.dev334.litelo.Interfaces;

import com.dev334.litelo.model.TimelineModel;

import java.util.List;

public interface PassDataInterface {
    void PassTodayEvents(List<TimelineModel> Events);
    void PassTomorrowEvents(List<TimelineModel> Events);
}
