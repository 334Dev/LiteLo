package com.dev334.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class upcomingAdapter extends RecyclerView.Adapter<upcomingAdapter.mViewholder>  {

    private List<clubModel> clubModels;

    public upcomingAdapter(List<clubModel> clubModels){
        this.clubModels=clubModels;
    }

    @NonNull
    @Override
    public upcomingAdapter.mViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_model, parent, false);
        return new upcomingAdapter.mViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull upcomingAdapter.mViewholder holder, int position) {

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

    public class mViewholder extends RecyclerView.ViewHolder{
        View view;
        TextView className, date, desc;
        public mViewholder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
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
    }
}
