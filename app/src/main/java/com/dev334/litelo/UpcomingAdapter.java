package com.dev334.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.mViewholder>  {

    private List<ClubModel> clubModels;
    private onNoteListener mOnNoteListener;
    public UpcomingAdapter(List<ClubModel> clubModels, onNoteListener mOnNoteListener){
        this.clubModels=clubModels;
        this.mOnNoteListener=mOnNoteListener;
    }

    @NonNull
    @Override
    public UpcomingAdapter.mViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_model, parent, false);
        return new UpcomingAdapter.mViewholder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.mViewholder holder, int position) {

        holder.setDate(clubModels.get(position).getTiming());
        holder.setDesc(clubModels.get(position).getDescription());
        holder.setName(clubModels.get(position).getTopic());
    }

    @Override
    public int getItemCount() {
        return clubModels.size();
    }
    public interface onNoteListener{
        void onNoteClick(int position);
    }

    public class mViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView className, date, desc;
        onNoteListener onNoteListener;
        public mViewholder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);
            view=itemView;
            itemView.setOnClickListener(this);
            this.onNoteListener=onNoteListener;
        }
        public void setName(String name){
            className=view.findViewById(R.id.className2);
            className.setText(name);
        }
        public void setDesc(String Desc){
            desc=view.findViewById(R.id.desc);
            desc.setText(Desc);
        }
        public void setDate(String Date){
            date=view.findViewById(R.id.dateTime);
            date.setText(Date);
        }

        @Override
        public void onClick(View v) {
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }
}
