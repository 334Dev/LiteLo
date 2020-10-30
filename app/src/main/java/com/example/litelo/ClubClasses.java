package com.example.litelo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClubClasses extends AppCompatActivity implements com.example.litelo.clubAdapter.SelectedItem {
    
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
        clubAdapter= new clubAdapter(clubModels,this);
        clubRecycler.setAdapter(clubAdapter);
        clubRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        clubRecycler.setHasFixedSize(true);

        firestore=FirebaseFirestore.getInstance();

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
                    clubAdapter.notifyDataSetChanged();
                }
            }
        });
        
    }

    @Override
    public void selectedItem(subjectModel model) {
        //future
    }
}