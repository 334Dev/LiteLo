package com.dev334.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubjectAttendance extends AppCompatActivity implements subjectAdapter.onNoteListener {
    private String UserID;
    private FirebaseAuth mAuth;
    private RecyclerView subjectRecycler;
    private FirebaseFirestore firestore;
    private subjectAdapter adapter;
    private List<subjectModel> subjectModels;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_show);
        subjectRecycler=findViewById(R.id.subjectRecycler);

        subjectModels=new ArrayList<>();
        adapter= new subjectAdapter(subjectModels,this);
        subjectRecycler.setAdapter(adapter);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subjectRecycler.setHasFixedSize(true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        createRecView();

    }

    private void createRecView() {
        UserID=mAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(UserID).collection("Classes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.isEmpty()){
                    Log.i("subjectList", "onSuccess: Empty");
                }
                else {
                    List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot snapshot:snapshots){
                        subjectModels.add(snapshot.toObject(subjectModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }


    @Override
    public void onNoteClick(int position) {
        String Subject=subjectModels.get(position).getDocumentId();
        Intent i=new Intent(SubjectAttendance.this,AttendanceHistory.class);
        i.putExtra("Subject",Subject);
        startActivity(i);
    }

}