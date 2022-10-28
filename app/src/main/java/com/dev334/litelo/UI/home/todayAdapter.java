package com.dev334.litelo.UI.home;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.TimelineModel;

import java.util.Calendar;
import java.util.List;

public class todayAdapter extends RecyclerView.Adapter<todayAdapter.mViewHolder>{
    private List<TimelineModel> timelineModelList;
    private ClickInterface Listener;
    private Context context;

    public todayAdapter(List<TimelineModel> timelineModelList, ClickInterface Listener, Context context){
        this.timelineModelList = timelineModelList;
        this.Listener=Listener;
        this.context=context;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_square, parent, false);
        return new todayAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setEventName(timelineModelList.get(position).getName());
        holder.setEventTime(timelineModelList.get(position).getTime());
        holder.setEventParent(timelineModelList.get(position).getParent());
        holder.setEventLocation("MS Teams");
    }

    @Override
    public int getItemCount() {
        return timelineModelList.size();
    }

    public interface ClickInterface {
        void recyclerviewOnClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView addCalender;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            addCalender=view.findViewById(R.id.s_eventcard_calender);

            addCalender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add to calender
                    TimelineModel currentEvent= timelineModelList.get(getAdapterPosition());

                    String date= currentEvent.getDate();
                    String eDate=date.substring(8);
                    String eMonth=date.substring(5,7);
                    Integer eMonthInt=Integer.parseInt(eMonth);
                    Integer eDateInt=Integer.parseInt(eDate);
                    String time=currentEvent.getTime();
                    String eHour=time.substring(0,2);
                    String eMin=time.substring(3);
                    Integer eHourInt=Integer.parseInt(eHour);
                    Integer eMinInt=Integer.parseInt(eMin);

                    Calendar beginTime = Calendar.getInstance();
                    Log.i("AddCalenderLog", "onClick: "+eDateInt);

                    beginTime.set(2021, 11, eDateInt, eHourInt, eMinInt);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(2021, 11, eDateInt, 23, 00);

                    Intent intent = new Intent(Intent.ACTION_INSERT)
                             .setData(CalendarContract.Events.CONTENT_URI)
                             .putExtra(CalendarContract.Events.TITLE,currentEvent.getName().toString())
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime.getTimeInMillis())
                             .putExtra(CalendarContract.Events.DESCRIPTION,currentEvent.getParent().toString())
                             .putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLink().toString());

                              context.startActivity(intent);

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.recyclerviewOnClick(getAdapterPosition());
                }
            });
        }

        public void setEventName(String Event){
            TextView event=view.findViewById(R.id.s_eventcard_event);
            event.setText(Event);
        }

        public void setEventLocation(String Location){
            TextView event=view.findViewById(R.id.s_eventcard_location);
            event.setText(Location);
        }

        public void setEventTime(String Date){
            TextView event=view.findViewById(R.id.s_eventcard_time);
            event.setText(Date);
        }

        public void setEventParent(String Parent){
            TextView parent=view.findViewById(R.id.s_eventcard_parent);
            parent.setText(Parent);
        }

    }
}
