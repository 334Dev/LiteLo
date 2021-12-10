package com.dev334.litelo.UI.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;

import java.util.List;

public class filterAdapter extends RecyclerView.Adapter<filterAdapter.mViewHolder>{
    private List<EventModel> eventModelList;
    private ClickInterface Listener;
    public filterAdapter(List<EventModel> eventModelList, ClickInterface Listener){
        this.eventModelList=eventModelList;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_rectangle, parent, false);
        return new filterAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setEventName(eventModelList.get(position).getName());
        holder.setEventTime("6:30 pm");
        holder.setEventParent(eventModelList.get(position).getParent());
        //holder.setEventLocation(eventModelList.get(position).getLink());
        holder.setEventLocation("MS Teams");
    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    public interface ClickInterface {
        void filterViewOnClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        View view;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.filterViewOnClick(getAdapterPosition());
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
        public void setEventTime(String Date){
            TextView event=view.findViewById(R.id.r_eventcard_time);
            event.setText(Date);
        }
        public void setEventParent(String Parent){
            TextView parent=view.findViewById(R.id.r_eventcard_parent);
            parent.setText(Parent);
        }

    }
}
