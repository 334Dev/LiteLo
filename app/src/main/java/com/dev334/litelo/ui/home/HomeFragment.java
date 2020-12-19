package com.dev334.litelo.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.dev334.litelo.ClubClasses;
import com.dev334.litelo.R;
import com.dev334.litelo.SubjectAttendance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment {

    private static final String TAG ="OfflineFirestore" ;
    private static final String TAG2 ="DayDebug" ;
    private AlertDialog.Builder builder;
    private AlertDialog show;
    private HomeViewModel homeViewModel;
    private FirebaseFirestore firestore;
    private TextView className, className2, disc, disc2, timeDate, timeDate2,subjectName, subjectDesc,attText;
    private List<AttendanceModel> attendanceModels;
    private AttendenceAdaptor attendenceAdaptor;
    private String group;
    private String UserID;
    private FirebaseAuth mAuth;
    private List<String> todayClass;
    private List<Double> timing;
    private ViewPager2 viewPager;
    private Button presentAll,classesAll;
    private Map<String, Object> mMap;
    private String[] groupsMech, groupsChem;
    //SlideUp
    private CircularSeekBar SlideSeekBar;
    private TextView TotalAttend, TotalNotAttend, Remaining, SlidePercentage;
    private ImageView Attendplus, NotAttendplus, Attendminus, NotAttendminus;

    private SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances
        firestore=FirebaseFirestore.getInstance();

        mAuth= FirebaseAuth.getInstance();

        //loading dailog
        builder=new AlertDialog.Builder(getContext());
        builder.setView(R.layout.loading_dailog);
        builder.setCancelable(true);
        show = builder.show();
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Club classes
        classesAll=root.findViewById(R.id.classAll);
        classesAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ClubClasses.class);
                startActivity(i);
            }
        });

        className=root.findViewById(R.id.className);
        disc=root.findViewById(R.id.desc);
        timeDate=root.findViewById(R.id.dateTime);

        className2=root.findViewById(R.id.className2);
        disc2=root.findViewById(R.id.desc2);
        timeDate2=root.findViewById(R.id.dateTime2);

        //lower subject and hint
        subjectDesc=root.findViewById(R.id.subjectDesc);
        subjectName=root.findViewById(R.id.subjectName);

        viewPager=root.findViewById(R.id.viewPagerAtt);
        presentAll=root.findViewById(R.id.presentAll);

        //SlideBar
        SlideSeekBar=root.findViewById(R.id.slideSeekBar);
        SlideSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Remaining=root.findViewById(R.id.remaining);
        TotalAttend=root.findViewById(R.id.slideTotalAttend);
        TotalNotAttend=root.findViewById(R.id.slideTotalNotAttend);
        Attendplus=root.findViewById(R.id.attendPlus);
        Attendminus=root.findViewById(R.id.AttendNot);
        NotAttendminus=root.findViewById(R.id.NotAttendNot);
        NotAttendplus=root.findViewById(R.id.NotAttendPlus);
        SlidePercentage=root.findViewById(R.id.SlidePercentage);

        UserID=mAuth.getCurrentUser().getUid();

        group=getActivity().getIntent().getStringExtra("Group_Name");

        getSubjectList();

        checkDate();

        setCcClasses();

        checkHoliday();

        presentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SubjectAttendance.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void getSubjectList() {
        mMap=new HashMap<>();
        firestore.collection("TimeTable").document(group).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mMap=documentSnapshot.getData();
                Log.i(TAG2, "onSuccess: getSubjectList");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG2, "onFailure: "+e.getMessage());
            }
        });
    }

    private void checkHoliday() {
        firestore.collection("TimeTable").document(group).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Calendar calendar=Calendar.getInstance();
                String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                List<String> holidays= (List<String>) documentSnapshot.get("holiday");
                if(holidays.contains(currentDate)){
                    Toast.makeText(getActivity(),"Marked as Holiday by CR",Toast.LENGTH_SHORT).show();
                }else{
                    getTodaysClass();
                }
            }
        });
    }

    //Check and compare device and server date
    private void checkDate() {
        UserID=mAuth.getCurrentUser().getUid();
        Calendar date= Calendar.getInstance();
        final String deviceDay=date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        Log.i("Date", "checkDate: "+deviceDay);

        firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String ServerDate= documentSnapshot.getString("Date");
                if(ServerDate.contentEquals(deviceDay)){
                    Log.i(TAG2, "onSuccess: Same- checkDate");
                    firestore.disableNetwork().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "onSuccess: Successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());    
                        }
                    });
                }else{
                    Log.i(TAG2, "onSuccess: Different-checkDate");
                    //if different calling method newDayChanges
                    newDayChanges(deviceDay);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG2, "onFailure: "+e.getMessage());
            }
        });
    }
    //newDayChanges-> setting all the subject's present absent status back to false
    private void newDayChanges(final String deviceDay) {
        Map<String, Object> map = new HashMap<>();
        map.put("absentStatus", false);
        map.put("presentStatus", false);
        final String[] mechClasses={"Physics(L)","Physics(T)","Maths(L)","Maths(T)","ELC(L)","Mechanics(L)","Mechanics(T)",
                "ELC(T)","Language(P)","Workshop(P)","Workshop(L)","Physics(P)","Mechanics(P)"};
        final String[] chemClasses={"Physics(L)","Physics(T)","Maths(L)","Maths(T)","CS(L)","Chemistry(L)","Chemistry(T)",
                "CS(T)","CSW(L)","ED(P)","ED(L)","CS(P)","Chemistry(P)"};

        groupsMech= new String[]{"A1","A2","B1","B2","C1","C2","D1","D2","E1","E2"};
        groupsChem=new String[]{"F1","F2","G1","G2","H1","H2","J1","J2","I1","I2"};

        if(Arrays.asList(groupsMech).contains(group)){
            for(String data:mechClasses) {
                firestore.collection("Users").document(UserID).collection("Classes").document(data)
                        .update("absentStatus", false, "presentStatus", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("newDayChanges", "onSuccess: newDayChanges");
                        //after changing to false setting server day to today's day
                        setserverDate(deviceDay);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("newDateChanges", "onFailure: newDayChanges");
                    }
                });
            }
        }else{
            for(String data:chemClasses) {
                firestore.collection("Users").document(UserID).collection("Classes").document(data)
                        .update("absentStatus", false, "presentStatus", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("newDayChanges", "onSuccess: newDayChanges");
                        //after changing to false setting server day to today's day
                        setserverDate(deviceDay);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("newDateChanges", "onFailure: newDayChanges");
                    }
                });
            }
        }

    }
    //setServerDay-> updating day
    private void setserverDate(String deviceDay) {
        firestore.collection("Users").document(UserID).update("Date", deviceDay).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("setserverDate", "onSuccess: Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("setserverDate", e.getMessage());
            }
        });
    }
    //getting today's academic classes from database
    private void getTodaysClass() {
        UserID=mAuth.getCurrentUser().getUid();

        Calendar date= Calendar.getInstance();
        final String deviceDay=date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        firestore.collection("TimeTable").document(group).collection(deviceDay)
                .whereEqualTo("isToday",true)
                //.orderBy("Time", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList= queryDocumentSnapshots.getDocuments();
                todayClass= new ArrayList<String>();
                timing= new ArrayList<Double>();
                //iterating through all the documents and storing data in arrayList todayClass nad timing
                for(DocumentSnapshot snapshot:snapshotList){
                    todayClass.add(snapshot.getString("Subject"));
                    timing.add(snapshot.getDouble("Time"));
                }

                Log.i("subjectMismatch", "setTodaysClass: "+timing);
                Log.i("subjectMismatch", "setTodaysClass: "+todayClass);
                Log.i(TAG2, "onSuccess: "+todayClass.size()+" "+timing.size());
                //after getting today's classes setting classes based on userProfile data
                show.dismiss();
                setTodaysClass();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fail1", "onFailure: timing and subject");
            }
        });

    }


    //setting today's class with personalized data
    private void setTodaysClass() {
            firestore.collection("Users").document(UserID).collection("Classes")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                            } else {
                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                attendanceModels = new ArrayList<>();
                                for (DocumentSnapshot snapshot : snapshotList) {
                                    if (todayClass.contains(snapshot.getId())) {
                                        //storing data in attendance model class
                                        attendanceModels.add(snapshot.toObject(AttendanceModel.class));
                                    }
                                }
                                for (int i = 0; i < attendanceModels.size(); i++) {
                                    Log.i("subjectMismatch", "setTodaysClass: " + attendanceModels.get(i).getSubject());
                                }
                                Log.i(TAG2, "onSuccess: " + attendanceModels.size());
                                //setting up adapter class and assigning this adapter to viewPager
                                attendenceAdaptor = new AttendenceAdaptor(attendanceModels, timing, todayClass);
                                viewPager.setAdapter(attendenceAdaptor);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
                }
            });

        //View pager basic settings
        viewPager.setPadding(150,0,150,0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(4);
        //OnPageSelected
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                subjectName.setText(todayClass.get(position));
                final Double present=attendanceModels.get(position).getPresent();
                final Double absent=attendanceModels.get(position).getAbsent();
                //calling setHint function to set up
                setHint(present,absent,position);
            }
        });

        firestore.enableNetwork().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "onSuccess: Back Online");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });

    }
    //setting club classes
    private void setCcClasses() {
        firestore.collection("ClubClasses").document("CC").collection("Classes")
                .whereEqualTo("visibility", "show")
                .whereGreaterThan("close",System.currentTimeMillis())
                .orderBy("close", Query.Direction.ASCENDING)
                .limit(2)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.i("ClubClasses", "onSuccess: Empty");
                    className.setText("No Meeting");
                    className2.setText("No Meeting");
                }else {
                    List<DocumentSnapshot> snapshotsList = queryDocumentSnapshots.getDocuments();
                    className.setText(snapshotsList.get(0).getString("Topic"));
                    disc.setText(snapshotsList.get(0).getString("Description"));
                    timeDate.setText(snapshotsList.get(0).getString("Timing"));
                    if(queryDocumentSnapshots.size()==2) {
                        className2.setText(snapshotsList.get(1).getString("Topic"));
                        disc2.setText(snapshotsList.get(1).getString("Description"));
                        timeDate2.setText(snapshotsList.get(1).getString("Timing"));
                    }else{
                        className2.setText("No Meeting");
                    }
                }

            }
        });
    }
    //setting hint
    public void setHint(Double present, Double absent,Integer position){
        Integer Present=present.intValue();
        Integer Absent=absent.intValue();

        Integer ClassesHeld= Present+Absent;
        Integer TotalClass=54;;
        Integer Progress=80;
        Integer consClass;
        consClass=((Absent * 100) / (100 - Progress))-ClassesHeld;
        if(Progress==80){
            if(consClass<0){
                subjectDesc.setText("You can miss next "+"the"+" classes.");
            }
            else if(consClass<=TotalClass-ClassesHeld){
                subjectDesc.setText("Go in the next " + Integer.toString(consClass) + " to come back on track.");
            }
            else{
                if(absent==0){
                    subjectDesc.setText("You'll have to attend all classes.");
                }
                else{
                    subjectDesc.setText("Not possible.");
                }
            }
        }
        setSwipeUp(present,absent,position);
    }

    //setting lower card view details
    private Double percentage=0.0,attend=0.0,notattend=0.0,SwipeAbsent, SwipePresent,totalclass=0.0,tempRemain;
    public void setSwipeUp(final Double present, Double absent,Integer position) {
        final String subject=todayClass.get(position);
        Object Class= mMap.get(subject);
        totalclass=Double.parseDouble(Class.toString());
        final Double remain = totalclass - present - absent;
        tempRemain=remain;
        Remaining.setText(remain.toString());
        percentage = calculatePer(present, absent);
        Integer inPer=percentage.intValue();
        SlidePercentage.setText(inPer.toString());
        SlideSeekBar.setMax(100);
        SlideSeekBar.setProgress(Float.parseFloat(percentage.toString()));
        SwipeAbsent = absent;
        SwipePresent = present;
        Attendplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attend+notattend+1<=remain && attend>=0 && notattend>=0) {
                    attend = attend + 1;
                    TotalAttend.setText(attend.toString());
                    tempRemain=tempRemain-1;
                    Remaining.setText(tempRemain.toString());
                    SwipePresent = SwipePresent + 1;
                    SlideSeekBar.setProgress(Float.parseFloat(calculatePer(SwipePresent, SwipeAbsent).toString()));
                    Integer inPer=calculatePer(SwipePresent,SwipeAbsent).intValue();
                    SlidePercentage.setText(inPer.toString());
                }
            }
        });
        NotAttendplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attend+notattend+1<=remain && attend>=0 && notattend>=0) {
                    notattend = notattend + 1;
                    TotalNotAttend.setText(notattend.toString());
                    tempRemain=tempRemain-1;
                    Remaining.setText(tempRemain.toString());
                    SwipeAbsent = SwipeAbsent + 1;
                    SlideSeekBar.setProgress(Float.parseFloat(calculatePer(SwipePresent, SwipeAbsent).toString()));
                    Integer inPer=calculatePer(SwipePresent,SwipeAbsent).intValue();
                    SlidePercentage.setText(inPer.toString());
                }
            }
        });
        Attendminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attend+notattend-1<remain && attend-1>=0 && notattend>=0) {
                    attend = attend - 1;
                    TotalAttend.setText(attend.toString());
                    tempRemain=tempRemain+1;
                    Remaining.setText(tempRemain.toString());
                    SwipePresent = SwipePresent - 1;
                    SlideSeekBar.setProgress(Float.parseFloat(calculatePer(SwipePresent, SwipeAbsent).toString()));
                    Integer inPer=calculatePer(SwipePresent,SwipeAbsent).intValue();
                    SlidePercentage.setText(inPer.toString());
                }
            }
        });
        NotAttendminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attend+notattend-1<remain && attend>=0 && notattend-1>=0) {
                    notattend = notattend - 1;
                    TotalNotAttend.setText(notattend.toString());
                    tempRemain=tempRemain+1;
                    Remaining.setText(tempRemain.toString());
                    SwipeAbsent = SwipeAbsent - 1;
                    SlideSeekBar.setProgress(Float.parseFloat(calculatePer(SwipePresent, SwipeAbsent).toString()));
                    Integer inPer=calculatePer(SwipePresent,SwipeAbsent).intValue();
                    SlidePercentage.setText(inPer.toString());
                }
            }
        });
    }

    public Double calculatePer(Double present,Double absent){
        return present * 100 / (present + absent);
    }


}
