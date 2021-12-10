package com.dev334.litelo.Interfaces;

import com.dev334.litelo.UI.home.EventModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PassDataInterface {
    void PassTodayEvents(List<EventModel> Events);
    void PassTomorrowEvents(List<EventModel> Events);
}
