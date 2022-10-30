package com.dev334.litelo.UI.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.TimelineModel;
import com.github.vipulasri.timelineview.TimelineView;

import org.w3c.dom.Text;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder> {
    private final List<TimelineModel> timelineModelList;
    private final TimelineAdapter.ClickInterface Listener;
    private final Context context;

    public TimelineAdapter(List<TimelineModel> timelineModelList, ClickInterface listener, Context context) {
        this.timelineModelList = timelineModelList;
        Listener = listener;
        this.context = context;
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
                timelineModelList.get(position).getDate(), timelineModelList.get(position).getLink());
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
        private TextView descText, eventLink, dateText;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
            descText = itemView.findViewById(R.id.text_timeline_title);
            dateText = itemView.findViewById(R.id.text_timeline_date);
            eventLink = itemView.findViewById(R.id.event_link);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listener.eventViewOnClick(getAdapterPosition());
                }
            });
            eventLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (URLUtil.isValidUrl(timelineModelList.get(getAdapterPosition()).getLink())) {
                        String url = timelineModelList.get(getAdapterPosition()).getLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                }
            });
        }

        public void setDetails(String desc, String date, String link) {
            descText.setText(desc);
            dateText.setText(date);
            if (link.equals("")) eventLink.setVisibility(View.GONE);
            else eventLink.setVisibility(View.VISIBLE);
            if (URLUtil.isValidUrl(link)) {
                eventLink.setText("Event Link");
            } else {
                eventLink.setText(link);
            }
        }
    }


}
