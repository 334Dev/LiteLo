package com.example.litelo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class StudentAdapter extends FirestoreRecyclerAdapter<student,StudentAdapter.StudentHolder> {

    public StudentAdapter(@NonNull FirestoreRecyclerOptions<student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentHolder holder, int position, @NonNull student model) {
        if(position==0)
        holder.sub.setText("Language Lab");
        if(position==1)
            holder.sub.setText("Maths");
        if(position==2)
            holder.sub.setText("Mechanics");
        if(position==3)
            holder.sub.setText("Physics");
        if(position==4)
            holder.sub.setText("Physics(P)");
        if(position==5)
            holder.sub.setText("Workshop");
      holder.pre.setText("Present days : "+String.valueOf(model.getPresent()));
      holder.ab.setText("Absent days : "+String.valueOf(model.getAbsent()));
        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Integer TotalClass = model.getPresent() + model.getAbsent();
        Integer Present=model.getPresent();
        if(TotalClass==0){
            holder.seekBar.setMax(100);
            holder.seekBar.setProgress(0);
            holder.attText.setText("0%");
        }
        else {
            Double percentage = Double.valueOf(model.getPresent() * 100 / (model.getPresent() + model.getAbsent()));

            Integer Perc = percentage.intValue();
            holder.attText.setText(Perc.toString() + "%");
            holder.seekBar.setMax(TotalClass);
            holder.seekBar.setProgress(Present);
            if (Perc >= 75)
                holder.seekBar.setCircleProgressColor(Color.parseColor("#1DB824"));
            else
                holder.seekBar.setCircleProgressColor(Color.parseColor("#FF7597"));
        }
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentHolder(v);
    }

    class StudentHolder extends RecyclerView.ViewHolder{
        TextView sub,pre,ab,attText;
        CircularSeekBar seekBar;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);
            sub=itemView.findViewById(R.id.subject_name);
            pre=itemView.findViewById(R.id.present_day);
            ab=itemView.findViewById(R.id.absent_day);
            attText=itemView.findViewById(R.id.attText2);
            seekBar=itemView.findViewById(R.id.Attseekbar2);
        }
    }
}
