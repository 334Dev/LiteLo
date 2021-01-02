package com.dev334.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class SubscribedClubs extends AppCompatActivity implements subResAdapter.onNoteListener {

    private Button floatingAdd;
    private List<String> subscribedClubs;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private TextView subscribeStatus;
    private RecyclerView clubRecycler;
    private subResAdapter clubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_clubs);

        subscribeStatus=findViewById(R.id.subscribeStatus);
        subscribeStatus.setVisibility(View.INVISIBLE);
        clubRecycler=findViewById(R.id.SubscribedRecycler);
        floatingAdd=findViewById(R.id.fab_add);
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SubscribedClubs.this, BrowseClubContainer.class);
                startActivity(i);
            }
        });
        subscribedClubs=new ArrayList<>();

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.get("subscribedClubs")!=null){
                    subscribedClubs= (List<String>) value.get("subscribedClubs");
                    setupRecyclerView();
                }else{
                    subscribeStatus.setVisibility(View.VISIBLE);
                    Log.i("subscribedClubs", "onEvent: Field Not Present");
                }
            }
        });
    }

    private void setupRecyclerView() {
        if(subscribedClubs.isEmpty()){
            subscribeStatus.setVisibility(View.VISIBLE);
        }else{
            clubAdapter= new subResAdapter(subscribedClubs, this);
            clubRecycler.setAdapter(clubAdapter);
            clubRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            clubRecycler.setHasFixedSize(true);
        }
    }

    @Override
    public void onNoteClick(int position) {
        //
    }
}