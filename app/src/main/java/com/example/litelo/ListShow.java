package com.example.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListShow extends AppCompatActivity {
    private String UserID;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private StudentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_show);



        mAuth=FirebaseAuth.getInstance();
        createRecView();

    }

    private void createRecView() {
        UserID=mAuth.getCurrentUser().getUid();

        // taking userId and and its classes list

        Query query=db.collection("/Users/"+UserID+"/Classes");

        // Setting RecyclerViewAdapter

        FirestoreRecyclerOptions<student> options= new FirestoreRecyclerOptions.Builder<student>()
                .setQuery(query,student.class).build();
        adapter=new StudentAdapter(options);
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //if App is in use
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //if App is in background
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}