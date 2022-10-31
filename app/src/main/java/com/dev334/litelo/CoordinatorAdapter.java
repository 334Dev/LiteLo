package com.dev334.litelo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.model.EventCoordinator;

import java.util.List;

public class CoordinatorAdapter extends RecyclerView.Adapter<CoordinatorAdapter.CustomVH> {

    private List<EventCoordinator> eventCoordinators;
    private Context context;

    public CoordinatorAdapter(List<EventCoordinator> eventCoordinators, Context context) {
        this.eventCoordinators = eventCoordinators;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.coordinator_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setView(eventCoordinators.get(position));
    }

    @Override
    public int getItemCount() {
        return eventCoordinators.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView mobile;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.mobile);
            mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = eventCoordinators.get(getAdapterPosition()).getUser().getMobile();
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + number));
                    context.startActivity(phoneIntent);
                }
            });
        }

        public void setView(EventCoordinator coordinator) {
            name.setText(coordinator.getUser().getName());
            mobile.setText(coordinator.getUser().getMobile());
        }
    }
}
