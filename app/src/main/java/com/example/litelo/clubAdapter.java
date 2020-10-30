package com.example.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class clubAdapter extends RecyclerView.Adapter<clubAdapter.mViewHolder> {

    private List<clubModel> clubModels;
    private clubAdapter.SelectedItem selectedItem;
    public clubAdapter(List<clubModel> clubModels, clubAdapter.SelectedItem selectedItem){
        this.clubModels=clubModels;
        this.selectedItem=selectedItem;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_model, parent, false);
        return new clubAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setDate(clubModels.get(position).getTiming());
        holder.setDesc(clubModels.get(position).getDescription());
        holder.setName(clubModels.get(position).getTopic());
    }

    @Override
    public int getItemCount() {
        return clubModels.size();
    }

    public interface SelectedItem{
        void selectedItem(subjectModel model);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView className, date, desc;

        public mViewHolder(@NonNull View itemView) {
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
