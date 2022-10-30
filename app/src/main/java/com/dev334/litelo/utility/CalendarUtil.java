package com.dev334.litelo.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.dev334.litelo.model.TimelineModel;

import java.util.Calendar;
import java.util.Map;
import java.util.StringTokenizer;

public class CalendarUtil {
    public void addEvent(Context context, Map<String, Object> map) {
        Calendar c = getCalendar(map);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, (String) map.get("name"));
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, c.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
        intent.putExtra(CalendarContract.Events.DESCRIPTION, (String) map.get("name"));
        context.startActivity(intent);
    }

    private Calendar getCalendar(Map<String, Object> map) {
        StringTokenizer dateTokenizer = new StringTokenizer((String) map.get("date"), "-");
        StringTokenizer timeTokenizer = new StringTokenizer((String) map.get("time"), ":");
        Calendar c = Calendar.getInstance();
        c.set(
                Integer.parseInt(dateTokenizer.nextToken()),
                Integer.parseInt(dateTokenizer.nextToken()),
                Integer.parseInt(dateTokenizer.nextToken()),
                Integer.parseInt(timeTokenizer.nextToken()),
                Integer.parseInt(timeTokenizer.nextToken()));
        return c;
    }
}
