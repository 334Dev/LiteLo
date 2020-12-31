package com.dev334.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class clubAdapter extends RecyclerView.Adapter<clubAdapter.mViewHolder> {

    private List<clubModel> clubModels;
    private ClickInterface listner;

    public clubAdapter(List<clubModel> clubModels, ClickInterface listner){
        this.clubModels=clubModels;
        this.listner=listner;
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

    public interface ClickInterface {
        void recyclerviewOnClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView className, date, desc;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listner.recyclerviewOnClick(getAdapterPosition());

                }
            });
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
