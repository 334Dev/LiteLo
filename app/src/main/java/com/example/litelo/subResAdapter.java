package com.example.litelo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class subResAdapter extends RecyclerView.Adapter<subResAdapter.mViewholder> {

    private List<String> resourceModel;
    private onNoteListener mOnNoteListener;

    public subResAdapter(List<String> resourceModel,onNoteListener onNoteListener){
        this.resourceModel=resourceModel;
        this.mOnNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public mViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_grid, parent, false);
        return new subResAdapter.mViewholder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewholder holder, int position) {
        holder.setResourceName(resourceModel.get(position));
    }

    @Override
    public int getItemCount() {
        return resourceModel.size();
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

        public void setResourceName(String resname){
            Subject=view.findViewById(R.id.resourceSubject);
            Subject.setText(resname);
            Log.i("resSub", "setResourceName: "+resname);
        }

        @Override
        public void onClick(View view) {
            onNotelistener.onNoteClick(getAdapterPosition());
        }
    }

}
