package com.dev334.litelo.UI.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;

import java.util.List;
import java.util.Map;

public class branchAdapter extends RecyclerView.Adapter<branchAdapter.mViewHolder>{
    private List<Map<String,String>> branchList;
    private ClickInterface Listener;
    public branchAdapter(List<Map<String,String>> branchList, ClickInterface Listener){
        this.branchList=branchList;
        this.Listener=Listener;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_card, parent, false);
        return new branchAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setBranch(branchList.get(position).get("Name"),"xyz");
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public interface ClickInterface {
        void branchviewOnClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        View view;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.branchviewOnClick(getAdapterPosition());
                }
            });
        }

        public void setBranch(String branchName,String img){
            ImageView imageView = view.findViewById(R.id.branchCard_logo);
            TextView textView = view.findViewById(R.id.branchCard_name);
            textView.setText(branchName);
        }
    }
}

