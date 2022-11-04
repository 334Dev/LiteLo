package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.TimelineModel;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder> {
    private final List<TimelineModel> timelineModelList;
    private final TimelineAdapter.ClickInterface Listener;
    private final Context context;
    private final Boolean admin;
    private final String id;

    public TimelineAdapter(List<TimelineModel> timelineModelList, ClickInterface listener, Context context, Boolean admin, String id) {
        this.timelineModelList = timelineModelList;
        Listener = listener;
        this.context = context;
        this.admin = admin;
        this.id = id;
    }

    @NonNull
    @Override
    public TimelineAdapter.TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.timeline_card, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.TimeLineViewHolder holder, int position) {
        holder.setDetails(
                timelineModelList.get(position).getDesc(),
                timelineModelList.get(position).getDate(),
                timelineModelList.get(position).getTime(),
                timelineModelList.get(position).getLink());
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
        private ImageView delete;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
            descText = itemView.findViewById(R.id.text_timeline_title);
            dateText = itemView.findViewById(R.id.text_timeline_date);
            eventLink = itemView.findViewById(R.id.event_link);
            delete = itemView.findViewById(R.id.delete);
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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDeletion(getAdapterPosition());
                }
            });
        }

        public void setDetails(String desc, String date, String time, String link) {
            descText.setText(desc);
            dateText.setText(date + " at " + time);
            if (link.equals("")) eventLink.setVisibility(View.GONE);
            else eventLink.setVisibility(View.VISIBLE);
            if (URLUtil.isValidUrl(link)) {
                eventLink.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_link_24_red, 0, 0, 0);
                eventLink.setText("Event Link");
            } else {
                eventLink.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24_red, 0, 0, 0);
                eventLink.setText(link);
            }
            if (admin)
                delete.setVisibility(View.VISIBLE);
            else
                delete.setVisibility(View.GONE);
        }
    }

    private void confirmDeletion(int adapterPosition) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent(adapterPosition);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setTitle("Are you sure you want to delete event?")
                .setCancelable(false).create();
        alertDialog.show();
    }

    private void deleteEvent(int adapterPosition) {
        FirebaseFirestore.getInstance()
                .collection("Timeline")
                .document("Events")
                .collection(id)
                .document(timelineModelList.get(adapterPosition).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore.getInstance()
                                    .collection("DateWiseEvent")
                                    .document(timelineModelList.get(adapterPosition).getDate())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                List<Map<String, Object>> list = (List<Map<String, Object>>) task.getResult().get("Events");
                                                int i = 0;
                                                for (; i < list.size(); ++i) {
                                                    if (compare(list.get(i), timelineModelList.get(adapterPosition)))
                                                        break;
                                                    ;
                                                }
                                                list.remove(i);
                                                Map<String, List<Map<String, Object>>> updated = new HashMap<>();
                                                updated.put("Events", list);
                                                FirebaseFirestore.getInstance()
                                                        .collection("DateWiseEvent")
                                                        .document(timelineModelList.get(adapterPosition).getDate())
                                                        .set(updated)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                                                    timelineModelList.remove(adapterPosition);
                                                                    TimelineAdapter.this.notifyItemRemoved(adapterPosition);
                                                                } else {
                                                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean compare(Map<String, Object> map, TimelineModel timelineModel) {
        return timelineModel.getName().equals((String) map.get("name")) &&
                timelineModel.getDesc().equals((String) map.get("desc")) &&
                timelineModel.getDate().equals((String) map.get("date")) &&
                timelineModel.getLink().equals((String) map.get("link")) &&
                timelineModel.getParent().equals((String) map.get("parent")) &&
                timelineModel.getTime().equals((String) map.get("time"));
    }


}
