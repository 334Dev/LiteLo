package com.dev334.litelo.UI.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.TimeLineViewHolder> {
    private List<EventModel> eventModelList;
    private todayAdapter.ClickInterface Listener;
    public eventAdapter(List<EventModel> eventModelList, todayAdapter.ClickInterface Listener){
        this.eventModelList=eventModelList;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public eventAdapter.TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.event_detail_card, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull eventAdapter.TimeLineViewHolder holder, int position) {
        holder.setDetails(eventModelList.get(position).getName(),eventModelList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }
        public void setDetails(String name,String date){
            TextView nameText = itemView.findViewById(R.id.eventName_textView);
            TextView dateText = itemView.findViewById(R.id.time_textView);
            nameText.setText(name);
            dateText.setText(date);
        }
    }


}
