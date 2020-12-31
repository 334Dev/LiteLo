package com.dev334.litelo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClubClasses extends AppCompatActivity implements com.dev334.litelo.clubAdapter.ClickInterface {
    
    private RecyclerView clubRecycler;
    private FirebaseFirestore firestore;
    private List<clubModel> clubModels;
    private clubAdapter clubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_classes);
        
        clubRecycler=findViewById(R.id.clubRecycler);

        clubModels=new ArrayList<>();

        firestore=FirebaseFirestore.getInstance();


        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        firestore.collection("ClubClasses").document("CC").collection("Classes")
                .whereEqualTo("visibility", "show")
                .whereGreaterThan("close",System.currentTimeMillis())
                .orderBy("close", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.i("ClubClasses", "onSuccess: Empty");
                }else {
                    List<DocumentSnapshot> snapshots=queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot snapshot:snapshots){
                        clubModels.add(snapshot.toObject(clubModel.class));
                    }
                    setupAdapter();
                }
            }
        });
        
    }

    private void setupAdapter() {
        clubAdapter= new clubAdapter(clubModels, this);
        clubRecycler.setAdapter(clubAdapter);
        clubRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        clubRecycler.setHasFixedSize(true);
        clubAdapter.notifyDataSetChanged();
    }

    @Override
    public void recyclerviewOnClick(int position) {
        Log.i("recyclerViewOnClick", "recyclerviewOnClick: Clicked");
        String link=clubModels.get(position).getLink();
        if(link=="N/A"){
            Toast.makeText(getApplicationContext(),"No link provided by admin", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(i);
        }
    }
}