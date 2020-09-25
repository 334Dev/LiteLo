package com.example.litelo.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litelo.R;

import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class AttendenceAdaptor extends RecyclerView.Adapter<AttendenceAdaptor.mViewHolder> {

    private List<Double> timing;

    private List<AttendanceModel> attendanceModels;

    public AttendenceAdaptor(List<AttendanceModel> attendanceModels, List<Double> timing) {
        this.attendanceModels = attendanceModels;
        this.timing=timing;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_model, parent, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new AttendenceAdaptor.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        //Double time=timing.get(position);
        Double present=attendanceModels.get(position).getPresent();
        Double absent=attendanceModels.get(position).getAbsent();
        holder.setSeekBar(present,absent);
        //holder.setTime(time);

    }

    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        private CircularSeekBar seekBar;
        private TextView textView;
        private View view;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setSeekBar(Double present, Double absent){
            seekBar=view.findViewById(R.id.attSeekbar);
            float totalclass= (float) (present+absent);
            seekBar.setMax(100);
            seekBar.setProgress(75);
        }
        public void setTime(Double time){
            textView=view.findViewById(R.id.editTime);
            Double timeMax=time+1;
            textView.setText(time.toString()+":00-"+timeMax.toString()+":00");
        }
    }
}
