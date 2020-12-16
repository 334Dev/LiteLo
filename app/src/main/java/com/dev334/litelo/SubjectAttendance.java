package com.dev334.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
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

        MobileAds.initialize(this, "ca-app-pub-9915472110094523~2090670666");
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-9915472110094523/9933679275")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

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