package com.example.litelo.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litelo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class AttendenceAdaptor extends RecyclerView.Adapter<AttendenceAdaptor.mViewHolder> {

    private List<Double> timing;
    private static String TAG="DateArray";

    private List<AttendanceModel> attendanceModels;

    private List<String> todaysClass;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private CircularSeekBar seekBar;
    private TextView  attText;

    private ViewGroup hintViewgrp;
    int flag;
    Context context;
    public AttendenceAdaptor(List<AttendanceModel> attendanceModels, List<Double> timing, List<String> todaysClass) {
        this.attendanceModels = attendanceModels;
        this.timing=timing;
        this.todaysClass=todaysClass;
        Integer size=todaysClass.size();

    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_model, parent, false);
        hintViewgrp=parent;
        context=parent.getContext();
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new AttendenceAdaptor.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        Double time=timing.get(position);
        Double present=attendanceModels.get(position).getPresent();
        Double absent=attendanceModels.get(position).getAbsent();
        Boolean presentStatus=attendanceModels.get(position).getPresentStatus();
        Boolean absentStatus=attendanceModels.get(position).getAbsentStatus();
        String Subject=attendanceModels.get(position).getSubject();
        holder.setSeekBar(present,absent,presentStatus,absentStatus);
        holder.setTime(time, Subject);
    }


    @Override

    public int getItemCount() {
        return attendanceModels.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{


        private TextView textView;
        private View view;
        private ImageView preBtn, abeBtn;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            preBtn=view.findViewById(R.id.presentBtn);
            abeBtn=view.findViewById(R.id.absentBtn);



            preBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sound);
                        mediaPlayer.start();
                        increaseAtt(getAdapterPosition());

                }
            });
            abeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.failsound);
                        mediaPlayer.start();
                        decreaseAtt(getAdapterPosition());

                }
            });
        }
        public void setSeekBar(Double present, Double absent, Boolean presentStatus, Boolean absentStatus){

            seekBar=view.findViewById(R.id.attSeekbar);
            attText=view.findViewById(R.id.attText);
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            if(present+absent<=0){
                seekBar.setMax(100);
                seekBar.setProgress(0);
                attText.setText("0");
            }
            else {
                Float totalclass = present.floatValue() + absent.floatValue();
                Float Present = present.floatValue();
                seekBar.setMax(totalclass);
                seekBar.setProgress(Present);
                Double percentage = present * 100 / (present + absent);

                Integer Perc = percentage.intValue();
                attText.setText(Perc.toString());
                if (presentStatus) {
                    seekBar.setCircleProgressColor(Color.parseColor("#1DB824"));
                }
                else if (absentStatus){
                    seekBar.setCircleProgressColor(Color.parseColor("#FF7597"));
                }
            }

        }
        public void setTime(Double time, String subject){

            Integer Time=time.intValue();
            Integer timeMax=Time+1;
            //if(subject.charAt(subject.length()-1)==')' && subject.charAt(subject.length()-2)=='P'){
            //    timeMax=Time+3;
            //}

            textView=view.findViewById(R.id.editTime);
            textView.setText(Time.toString()+":00-"+timeMax.toString()+":00");
        }

    }

    private void decreaseAtt(final int adapterPosition) {
        mAuth=FirebaseAuth.getInstance();
        final String UserID=mAuth.getCurrentUser().getUid();
        final Integer[] i = {0};
        firestore=FirebaseFirestore.getInstance();


        final Boolean preStatus=attendanceModels.get(adapterPosition).getPresentStatus();
        final Boolean abStatus=attendanceModels.get(adapterPosition).getAbsentStatus();
        final Double present=attendanceModels.get(adapterPosition).getPresent();
        final Double absent=attendanceModels.get(adapterPosition).getAbsent();

        if(abStatus){
            Toast.makeText(hintViewgrp.getContext(), "You have already updated", Toast.LENGTH_LONG).show();
        }
        else  {

            DocumentReference reff=firestore.collection("Users").document(UserID).collection("Classes")
                    .document(todaysClass.get(adapterPosition));

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);

            if(preStatus){
                Map<String, Object> map = new HashMap<>();
                map.put("Present", present - 1);
                map.put("Absent", absent + 1);

                reff.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setStatus(adapterPosition, UserID, false, true);
                        Double Present=present-1;
                        Double Absent=absent+1;
                        attendanceModelChange(adapterPosition, Present,Absent,false, true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hintViewgrp.getContext(), "Failed to update", Toast.LENGTH_LONG).show();
                    }
                });


                reff.update("presentArray", FieldValue.arrayRemove(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: DateRemoved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });

                reff.update("absentArray", FieldValue.arrayUnion(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Absent Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });

            }
            else {
                Map<String, Object> map = new HashMap<>();
                map.put("Present", present);
                map.put("Absent", absent+1);

                reff.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setStatus(adapterPosition, UserID, false, true);
                        Double Absent=absent+1;
                        attendanceModelChange(adapterPosition, present,Absent, false, true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hintViewgrp.getContext(), "Failed to update", Toast.LENGTH_LONG).show();
                    }
                });

                reff.update("absentArray", FieldValue.arrayUnion(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Absent Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });
            }
        }
    }

    private void increaseAtt(final int adapterPosition) {
        mAuth=FirebaseAuth.getInstance();
        final String UserID=mAuth.getCurrentUser().getUid();
        final Integer[] i = {0};
        firestore=FirebaseFirestore.getInstance();


        final Boolean preStatus=attendanceModels.get(adapterPosition).getPresentStatus();
        final Boolean abStatus=attendanceModels.get(adapterPosition).getAbsentStatus();
        final Double present=attendanceModels.get(adapterPosition).getPresent();
        final Double absent=attendanceModels.get(adapterPosition).getAbsent();

        if(preStatus){
            Toast.makeText(hintViewgrp.getContext(), "You have already updated", Toast.LENGTH_LONG).show();
        }
        else  {
            DocumentReference reff=firestore.collection("Users").document(UserID).collection("Classes")
                    .document(todaysClass.get(adapterPosition));

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);


            if(abStatus){

                Map<String, Object> map = new HashMap<>();
                map.put("Present", present + 1);
                map.put("Absent", absent - 1);

                reff.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setStatus(adapterPosition, UserID, true, false);
                        Double Present=present+1;
                        Double Absent=absent-1;
                        attendanceModelChange(adapterPosition, Present,Absent,true, false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hintViewgrp.getContext(), "Failed to update", Toast.LENGTH_LONG).show();
                    }
                });

                reff.update("absentArray", FieldValue.arrayRemove(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: DateRemoved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });

                reff.update("presentArray", FieldValue.arrayUnion(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Present Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });

            }
            else {
                Map<String, Object> map = new HashMap<>();
                map.put("Present", present + 1);
                map.put("Absent", absent);

                reff.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setStatus(adapterPosition, UserID, true, false);
                        Double Present=present+1;
                        attendanceModelChange(adapterPosition, Present,absent, true, false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(hintViewgrp.getContext(), "Failed to update", Toast.LENGTH_LONG).show();
                    }
                });

                reff.update("presentArray", FieldValue.arrayUnion(todayString)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Present Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });
                
            }
        }

    }

    private void setStatus(int adapterPosition, String userID, Boolean preStatus, Boolean abStatus) {

        firestore.collection("Users").document(userID).collection("Classes")
                .document(todaysClass.get(adapterPosition)).update("presentStatus",preStatus,
                "absentStatus",abStatus);
    }

    private void attendanceModelChange(int adapterPosition, Double present,Double absent, Boolean preStatus, Boolean abStatus) {
        attendanceModels.get(adapterPosition).setPresent(present);
        attendanceModels.get(adapterPosition).setAbsent(absent);
        attendanceModels.get(adapterPosition).setPresentStatus(preStatus);
        attendanceModels.get(adapterPosition).setAbsentStatus(abStatus);
        notifyItemChanged(adapterPosition);
    }
    
}
