package com.example.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class AttendanceHistory extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private MCalendarView calendarView;
    private String UserID, Subject;
    private static String TAG="checkDate";
    private TextView subjectName;
    private List<String> presentArray, absentArray, cancelArray;
    private DocumentReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        calendarView=findViewById(R.id.calendarView);

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        Intent i=getIntent();
        Subject=i.getStringExtra("Subject");
        UserID=mAuth.getCurrentUser().getUid();

        getCalendarDates();

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                Log.i(TAG, "onDateClick: "+date.toString());
                final int year=date.getYear();
                final int month=date.getMonth();
                final int day=date.getDay();
                final String clickDate;
                if(day<10) {
                    clickDate = Integer.toString(year) + "-" + Integer.toString(month) + "-0" + Integer.toString(day);
                }else{
                    clickDate = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
                }
                Log.i(TAG, "onDateClick: "+clickDate);

                reff=firestore.collection("Users").document(UserID).collection("Classes")
                        .document(Subject);

                if(presentArray.contains(clickDate)){


                    //remove date from present array
                    reff.update("presentArray", FieldValue.arrayRemove(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            presentArray.remove(clickDate);
                            changePresent(-1);
                            Log.i(TAG, "onSuccess: "+presentArray.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });

                    reff.update("absentArray", FieldValue.arrayUnion(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            absentArray.add(clickDate);
                            calendarView.unMarkDate(year,month,day);
                            calendarView.setMarkedStyle(MarkStyle.BACKGROUND,getColor(R.color.colorRed));
                            calendarView.markDate(year,month,day);
                            changeAbsent(1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });

                }else if(absentArray.contains(clickDate)){

                    reff.update("absentArray", FieldValue.arrayRemove(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            absentArray.remove(clickDate);
                            changeAbsent(-1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());

                        }
                    });

                    reff.update("cancelArray", FieldValue.arrayUnion(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            cancelArray.add(clickDate);

                            calendarView.unMarkDate(year,month,day);
                            calendarView.setMarkedStyle(MarkStyle.BACKGROUND,getColor(R.color.colorPrimaryDark));
                            calendarView.markDate(year,month,day);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });


                }else if(cancelArray.contains(clickDate)){
                    reff.update("cancelArray", FieldValue.arrayRemove(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            cancelArray.remove(clickDate);
                            calendarView.unMarkDate(year,month,day);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });

                }else{

                    reff.update("presentArray", FieldValue.arrayUnion(clickDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            presentArray.add(clickDate);
                            calendarView.setMarkedStyle(MarkStyle.BACKGROUND,getColor(R.color.colorPrimary));
                            calendarView.markDate(year,month,day);
                            changePresent(1);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                        }
                    });
                }

            }
        });


    }


    private void getCalendarDates() {

        firestore.collection("Users").document(UserID).collection("Classes")
                .document(Subject).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                presentArray=new ArrayList<>();
                presentArray= (List<String>) documentSnapshot.get("presentArray");
                absentArray= (List<String>) documentSnapshot.get("absentArray");
                cancelArray= (List<String>) documentSnapshot.get("cancelArray");
                setPresentInCalender();
                setAbsentInCalender();
                setCancelInCalendar();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void setCancelInCalendar() {
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getColor(R.color.colorPrimaryDark));
        for(int i=0;i<cancelArray.size();i++){
            if(cancelArray.get(i).equalsIgnoreCase("demouser")){
                continue;
            }else{
                String date[]=cancelArray.get(i).split("-");
                int year= Integer.parseInt(date[0]);
                int month=Integer.parseInt(date[1]);
                int day=Integer.parseInt(date[2]);
                calendarView.markDate(year,month,day);
            }
        }
    }

    private void setAbsentInCalender() {
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getColor(R.color.colorRed));
        for(int i=0;i<absentArray.size();i++){
            if(absentArray.get(i).equalsIgnoreCase("demouser")){
                continue;
            }else{
                String date[]=absentArray.get(i).split("-");
                int year= Integer.parseInt(date[0]);
                int month=Integer.parseInt(date[1]);
                int day=Integer.parseInt(date[2]);
                calendarView.markDate(year,month,day);
            }
        }
    }

    private void setPresentInCalender() {
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getColor(R.color.colorPrimary));
        for(int i=0;i<presentArray.size();i++){
            if(presentArray.get(i).equalsIgnoreCase("demouser")){
                continue;
            }else{
                String date[]=presentArray.get(i).split("-");
                int year= Integer.parseInt(date[0]);
                int month=Integer.parseInt(date[1]);
                int day=Integer.parseInt(date[2]);
                calendarView.markDate(year,month,day);
            }
        }
    }
    private void changePresent(double i){
        reff.update("Present", FieldValue.increment(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //success
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
    private void changeAbsent(double i){

        reff.update("Absent", FieldValue.increment(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //success
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });

    }
}