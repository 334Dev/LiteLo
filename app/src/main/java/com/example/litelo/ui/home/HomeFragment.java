package com.example.litelo.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseFirestore firestore;
    private TextView className, className2, disc, disc2, timeDate, timeDate2;
    private List<AttendanceModel> attendanceModels;
    private AttendenceAdaptor attendenceAdaptor;
    private String group;
    private String UserID;
    private FirebaseAuth mAuth;
    private List<String> todayClass;
    private List<Double> timing;
    private ViewPager2 viewPager;

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

        viewPager=root.findViewById(R.id.viewPagerAtt);

        setCcClasses();

        getTodaysClass();


        return root;
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

                        attendenceAdaptor= new AttendenceAdaptor(attendanceModels,timing);
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
}