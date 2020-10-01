package com.example.litelo.ui.home;

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
    //SlideUp
    private CircularSeekBar SlideSeekBar;
    private TextView TotalAttend, TotalNotAttend, Remaining;
    private ImageView Attendplus, NotAttendplus, Attendminus, NotAttendminus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        firestore=FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        className=root.findViewById(R.id.className);
        disc=root.findViewById(R.id.desc);
        timeDate=root.findViewById(R.id.dateTime);

        className2=root.findViewById(R.id.className2);
        disc2=root.findViewById(R.id.desc2);
        timeDate2=root.findViewById(R.id.dateTime2);

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


        checkDate();

        setCcClasses();

        getTodaysClass();


        return root;
    }


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
                    newDayChanges(deviceDay);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

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
                //int i=0;
                todayClass= new ArrayList<String>();
                timing= new ArrayList<Double>();
                for(DocumentSnapshot snapshot:snapshotList){
                    todayClass.add(snapshot.getString("Subject"));
                    timing.add(snapshot.getDouble("Time"));

                }
                Log.i("timing", "onSuccess: "+timing.get(0));
                Log.i("timing", "onSuccess: "+todayClass.get(2));
                setTodaysClass();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fail1", "onFailure: timing and subject");
            }
        });



    }

    private void setTodaysClass() {
        firestore.collection("Users").document(UserID).collection("Classes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList=queryDocumentSnapshots.getDocuments();
                        attendanceModels=new ArrayList<>();
                        int i=0;
                        for(DocumentSnapshot snapshot: snapshotList) {
                                if(todayClass.contains(snapshot.getId())) {
                                    attendanceModels.add(snapshot.toObject(AttendanceModel.class));

                                }
                        }

                        attendenceAdaptor= new AttendenceAdaptor(attendanceModels,timing,todayClass);
                        viewPager.setAdapter(attendenceAdaptor);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        viewPager.setPadding(250,0,250,0);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                subjectName.setText(todayClass.get(position));
                final Double present=attendanceModels.get(position).getPresent();
                final Double absent=attendanceModels.get(position).getAbsent();
                setHint(present,absent);
            }
        });




    }

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

    public void setHint(Double present, Double absent){
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
        /*Double totalLectures= 56.0;
        Double remain=totalLectures-present-absent;
        Double percent=present*100/(absent+present);
        Double attend=0.0,notAttend=0.0;
        SlideSeekBar.setProgress(Float.parseFloat(percent.toString()));
        Remaining.setText(remain.intValue());
        Attendplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

}
