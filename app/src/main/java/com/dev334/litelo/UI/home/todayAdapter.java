package com.dev334.litelo.UI.home;

import static android.content.Intent.ACTION_EDIT;
import static android.content.Intent.ACTION_INSERT;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.clubAdapter;

import java.util.Calendar;
import java.util.List;


public class todayAdapter extends RecyclerView.Adapter<todayAdapter.mViewHolder>{
    private List<EventModel> eventModelList;
    private ClickInterface Listener;
    private Context context;

    public todayAdapter(List<EventModel> eventModelList, ClickInterface Listener,Context context){
        this.eventModelList=eventModelList;
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
        holder.setEventName(eventModelList.get(position).getName());
        holder.setEventTime("6:30 pm");
        holder.setEventParent(eventModelList.get(position).getParent());
        //holder.setEventLocation(" "+eventModelList.get(position).getLink());
        holder.setEventLocation("MS Teams");
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
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
                    EventModel currentEvent=eventModelList.get(getAdapterPosition());

                    String fulldate= currentEvent.getDate();
                    String eventdate=fulldate.substring(0,2);
                    Integer eventdateint=Integer.parseInt(eventdate);

                    Calendar beginTime = Calendar.getInstance();

                    beginTime.set(2021, 12, eventdateint, 10, 00);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(2021, 12, eventdateint, 18, 00);

                    Intent intent = new Intent(Intent.ACTION_INSERT)
                             .setData(CalendarContract.Events.CONTENT_URI)
                             .putExtra(CalendarContract.Events.TITLE,currentEvent.getName().toString())
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime.getTimeInMillis())
                             .putExtra(CalendarContract.Events.DESCRIPTION,currentEvent.getParent().toString())
                             .putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLink().toString());

                              context.startActivity(intent);

                 /*   if(intent.resolveActivity(context.getPackageManager())!=null)
                    {

                    }
                    else
                    {
                        Toast.makeText(context,"Not supported",Toast.LENGTH_SHORT).show();
                    }*/

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
