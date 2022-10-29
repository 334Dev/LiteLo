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
import com.dev334.litelo.utility.Constants;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomVH> {

    private List<NotificationModel> notifications;
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
        private TextView name, desc,time;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.notification_desc_tv);
            time = itemView.findViewById(R.id.date_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setView(NotificationModel notificationModel) {
            name.setText(notificationModel.getName());
            desc.setText(notificationModel.getDesc());

        }
    }
}
