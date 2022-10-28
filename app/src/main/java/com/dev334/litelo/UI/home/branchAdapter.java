package com.dev334.litelo.UI.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.DepartmentModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class branchAdapter extends RecyclerView.Adapter<branchAdapter.mViewHolder> {
    private List<DepartmentModel> departments;
    private ClickInterface Listener;
    Map<String, Object> images = new HashMap<>();

    public branchAdapter(List<DepartmentModel> departments, ClickInterface Listener, Map<String, Object> images) {
        this.departments = departments;
        this.Listener = Listener;
        this.images = images;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_card, parent, false);
        return new branchAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setBranch((String) departments.get(position).getName(), (Integer) images.get(departments.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public interface ClickInterface {
        void branchviewOnClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        View view;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Listener.branchviewOnClick(getAdapterPosition());
                }
            });
        }

        public void setBranch(String branchName, Integer img) {
            ImageView imageView = view.findViewById(R.id.branchCard_logo);
            imageView.setImageResource(img);
            TextView textView = view.findViewById(R.id.branchCard_name);
            textView.setText(branchName);
        }
    }
}

