package com.dev334.litelo.UI.home;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.TimelineModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder> {
    private final List<TimelineModel> timelineModelList;
    private final TimelineAdapter.ClickInterface Listener;

    public TimelineAdapter(List<TimelineModel> timelineModelList, TimelineAdapter.ClickInterface listener) {
        this.timelineModelList = timelineModelList;
        this.Listener = listener;
    }

    @NonNull
    @Override
    public TimelineAdapter.TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.timeline_card, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimeLineViewHolder holder, int position) {
        holder.setDetails(timelineModelList.get(position).getDesc(),
                timelineModelList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return timelineModelList.size();
    }

    public interface ClickInterface {
        void eventViewOnClick(int position);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listener.eventViewOnClick(getAdapterPosition());
                }
            });
        }

        public void setDetails(String desc, String date) {
            TextView descText = itemView.findViewById(R.id.text_timeline_title);
            TextView dateText = itemView.findViewById(R.id.text_timeline_date);
            descText.setText(desc);
            dateText.setText(date);
        }
    }


}
