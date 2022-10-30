package com.dev334.litelo.UI.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.EventActivity;
import com.dev334.litelo.EventAdapter;
import com.dev334.litelo.R;
import com.dev334.litelo.model.EventModel;
import com.dev334.litelo.model.NotificationModel;
import com.dev334.litelo.utility.Constants;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomVH> {

    private final List<NotificationModel> notifications;
    Context context;

    public NotificationAdapter(List<NotificationModel> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.CustomVH(View.inflate(parent.getContext(), R.layout.notification_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.CustomVH holder, int position) {
        holder.setView(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        private TextView name, time;
        private ExpandableTextView desc;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.notification_desc_tv).findViewById(R.id.notification_desc_tv);
            time = itemView.findViewById(R.id.date_time);
        }

        public void setView(NotificationModel notificationModel) {
            name.setText(notificationModel.getTitle());
            desc.setText(notificationModel.getBody());
            time.setText(format(notificationModel.getTime()));
        }
    }

    private String format(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        if (c.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
            return c.get(Calendar.DAY_OF_MONTH) +
                    " " +
                    c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                    " at " +
                    c.get(Calendar.HOUR) +
                    ":" +
                    appendZero(c.get(Calendar.MINUTE));
        return c.get(Calendar.DAY_OF_MONTH) +
                " " +
                c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                " " +
                c.get(Calendar.YEAR) +
                " at " +
                c.get(Calendar.HOUR) +
                ":" +
                appendZero(c.get(Calendar.MINUTE));
    }

    private String appendZero(int i) {
        if (i > 9)
            return String.valueOf(i);
        return "0" + i;
    }
}
