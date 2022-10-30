package com.dev334.litelo.UI.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.TimelineModel;
import com.dev334.litelo.utility.CalendarUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class FilterEventAdapter extends RecyclerView.Adapter<FilterEventAdapter.mViewHolder> {
    private List<Map<String, Object>> list;
    private Context context;

    public FilterEventAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FilterEventAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_rectangle, parent, false);
        return new FilterEventAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterEventAdapter.mViewHolder holder, int position) {
        holder.setView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        private TextView desc, time, event, link;
        private ImageView calendar, icon;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.time);
            event = itemView.findViewById(R.id.event);
            link = itemView.findViewById(R.id.event_link);
            calendar = itemView.findViewById(R.id.calendar);
            icon = itemView.findViewById(R.id.icon);
            calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CalendarUtil().addEvent(context, list.get(getAdapterPosition()));
                }
            });
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (URLUtil.isValidUrl((String) list.get(getAdapterPosition()).get("link"))) {
                        String url = (String) list.get(getAdapterPosition()).get("link");
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                }
            });
        }

        public void setView(Map<String, Object> mp) {
            desc.setText((String) mp.get("desc"));
            time.setText((String) mp.get("time"));
            event.setText((String) mp.get("name"));
            if (((String) mp.get("link")).equals("")) {
                link.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
            } else {
                link.setVisibility(View.VISIBLE);
                icon.setVisibility(View.VISIBLE);
            }
            if (URLUtil.isValidUrl((String) mp.get("link"))) {
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_link_24_red));
                link.setText("Event Link");
            } else {
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24_red));
                link.setText((String) mp.get("link"));
            }
        }

    }
}
