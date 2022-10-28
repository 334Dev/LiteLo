package com.dev334.litelo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.model.EventModel;
import com.dev334.litelo.utility.Constants;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CustomVH> {

    private List<EventModel> events;
    private Context context;

    public EventAdapter(List<EventModel> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(View.inflate(parent.getContext(), R.layout.event_detail_card, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.setView(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private AppCompatTextView name, tagline;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            tagline = itemView.findViewById(R.id.tagline);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtra(Constants.EVENT, events.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

        public void setView(EventModel eventModel) {
            name.setText(eventModel.getName());
            tagline.setText(eventModel.getTagline());
        }
    }
}
