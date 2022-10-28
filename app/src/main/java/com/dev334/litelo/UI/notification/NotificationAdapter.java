package com.dev334.litelo.UI.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.mViewholder> {

    private List<String> resourceModels;

    private onNoteListener mOnNoteListener;
    public NotificationAdapter(List<String> resourceModels, onNoteListener onNoteListener){
        this.resourceModels=resourceModels;
        this.mOnNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public mViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationAdapter.mViewholder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewholder holder, int position) {
//        holder.setSubjectName(resourceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return resourceModels.size();
    }
    public interface onNoteListener{
        void onNoteClick(int position);
    }

    public class mViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        TextView Subject;
        onNoteListener onNotelistener;
        public mViewholder(@NonNull View itemView, onNoteListener onNotelistener) {
            super(itemView);
            view=itemView;
            itemView.setOnClickListener(this);
            this.onNotelistener=onNotelistener;
        }


        @Override
        public void onClick(View view) {
            onNotelistener.onNoteClick(getAdapterPosition());
        }
    }

}
