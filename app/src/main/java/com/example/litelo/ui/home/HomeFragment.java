package com.example.litelo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.example.litelo.ListShow;
import com.example.litelo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment {

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
    private ImageView loadingWhite, presentBtn, absentBtn;
    private ProgressBar loadingBar;
    private Button presentAll;
    private Map<String, Object> mMap;
    //SlideUp
    private CircularSeekBar SlideSeekBar;
    private TextView TotalAttend, TotalNotAttend, Remaining, SlidePercentage;
    private ImageView Attendplus, NotAttendplus, Attendminus, NotAttendminus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances
        firestore=FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        //Club classes
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
        Remaining=root.findViewById(R.id.remaining);
        TotalAttend=root.findViewById(R.id.slideTotalAttend);
        TotalNotAttend=root.findViewById(R.id.slideTotalNotAttend);
        Attendplus=root.findViewById(R.id.attendPlus);
        Attendminus=root.findViewById(R.id.AttendNot);
        NotAttendminus=root.findViewById(R.id.NotAttendNot);
        NotAttendplus=root.findViewById(R.id.NotAttendPlus);
        SlidePercentage=root.findViewById(R.id.SlidePercentage);

        UserID=mAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                group=documentSnapshot.getString("Group");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("GetGroup", "onFailure: Failed");
            }
        });
        Log.i("GetGroup", "onCreateView: "+group);
        mMap=new HashMap<>();
        firestore.collection("TimeTable").document("E1").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mMap=documentSnapshot.getData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        checkDate();

        setCcClasses();

        getTodaysClass();
        presentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ListShow.class);
                startActivity(intent);
            }
        });

        return root;
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
                    Log.i("DateCheck", "onSuccess: Same");
                }else{
                    Log.i("DateCheck", "onSuccess: Different");
                    //if different calling method newDayChanges
                    newDayChanges(deviceDay);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //newDayChanges-> setting all the subject's present absent status back to false
    private void newDayChanges(final String deviceDay) {
        Map<String, Object> map = new HashMap<>();
        map.put("absentStatus", false);
        map.put("presentStatus", false);
        final String[] mechClasses={"Workshop","Mechanics","Language Lab", "Physics","Physics(P)","Maths"};
        for(String data:mechClasses) {
            firestore.collection("Users").document(UserID).collection("Classes").document(data)
                    .update("absentStatus", false,"presentStatus", false).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                Log.i("setserverDate", "onSuccess: Successful");
            }
        });
    }
    //getting today's academic classes from database
    private void getTodaysClass() {
        UserID=mAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                group=documentSnapshot.getString("Group");
            }
        });
        firestore.collection("TimeTable").document("E1").collection("Thursday")
                .whereEqualTo("isToday",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                //after getting today's classes setting classes based on userProfile data
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
        firestore.collection("Users").document(UserID).collection("Classes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                        attendanceModels=new ArrayList<>();
                        for(DocumentSnapshot snapshot: snapshotList) {
                                if(todayClass.contains(snapshot.getId())) {
                                    //storing data in attendance model class
                                    attendanceModels.add(snapshot.toObject(AttendanceModel.class));
                                }
                        }
                        //setting up adapter class and assigning this adapter to viewPager
                        attendenceAdaptor= new AttendenceAdaptor(attendanceModels,timing,todayClass);
                        viewPager.setAdapter(attendenceAdaptor);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //View pager basic settings
        viewPager.setPadding(250,0,250,0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

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

    }
    //setting club classes
    private void setCcClasses() {
        firestore.collection("ClubClasses").document("CC").collection("Classes")
                .whereEqualTo("visibility", "show").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotsList= queryDocumentSnapshots.getDocuments();
                int i=1;
                for(DocumentSnapshot snapshot: snapshotsList){
                    if(i==1) {
                        className.setText(snapshot.getString("Topic"));
                        disc.setText(snapshot.getString("Description"));
                        timeDate.setText(snapshot.getString("Timing"));
                    }
                    else if(i==2){
                        className2.setText(snapshot.getString("Topic"));
                        disc2.setText(snapshot.getString("Description"));
                        timeDate2.setText(snapshot.getString("Timing"));
                    }
                    else{
                        break;
                    }
                    i=i+1;
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
