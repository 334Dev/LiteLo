package com.dev334.litelo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class subjectResources extends AppCompatActivity implements subResAdapter.onNoteListener {

    private RecyclerView resSubRecycler;
    private subResAdapter adapter;
    private List<String> resourceModels;
    private List<String> Links;
    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    private String UserID;
    private TextView resSubName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_resources);
        resSubRecycler=findViewById(R.id.resSubRecycler);
        resSubName=findViewById(R.id.subResName);

        resourceModels=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Intent i=getIntent();
        String subject=i.getStringExtra("Subject");
        resSubName.setText(subject);
        fstore.collection("Resources").document(subject).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("resSub", "onSuccess: ");
                resourceModels = (List<String>) documentSnapshot.get("Type");
                Links= (List<String>) documentSnapshot.get("Link");
                setResources();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("resSub", "onFailure: "+e.getMessage());
            }
        });

    }

    private void setResources() {

        adapter= new subResAdapter(resourceModels,this);
        resSubRecycler.setAdapter(adapter);
        resSubRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        resSubRecycler.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onNoteClick(final int position) {
        String link=Links.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }
}