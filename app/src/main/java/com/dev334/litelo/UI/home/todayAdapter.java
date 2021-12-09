package com.dev334.litelo.UI.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.clubAdapter;

import java.util.List;


public class todayAdapter extends RecyclerView.Adapter<todayAdapter.mViewHolder>{
    private List<EventModel> eventModelList;
    private ClickInterface Listener;
    public todayAdapter(List<EventModel> eventModelList, ClickInterface Listener){
        this.eventModelList=eventModelList;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_rectangle, parent, false);
        return new todayAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setEventName(eventModelList.get(position).getName());
        holder.setEventDate("  "+eventModelList.get(position).getDate());
        holder.setEventLocation(" "+eventModelList.get(position).getLink());
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
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.recyclerviewOnClick(getAdapterPosition());
                }
            });
        }

        public void setEventName(String Event){
            TextView event=view.findViewById(R.id.r_eventcard_event);
            event.setText(Event);
        }
        public void setEventLocation(String Location){
            TextView event=view.findViewById(R.id.r_eventcard_location);
            event.setText(Location);
        }
        public void setEventDate(String Date){
            TextView event=view.findViewById(R.id.r_eventcard_date);
            event.setText(Date);
        }

    }
}
