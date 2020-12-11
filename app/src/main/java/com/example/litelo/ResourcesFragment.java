package com.example.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class ResourcesFragment extends Fragment implements resourceAdapter.onNoteListener {
    private View view;
    private RecyclerView resourceRecycler;
    private String[] resourceModels={"Mathematics","Physics","Chemistry","Engineering Mechanics","Workshop","Computer Science"};
    private resourceAdapter adapter;
    private FirebaseFirestore fstore;
    private List<String> resourceSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resources, container, false);
        resourceRecycler=view.findViewById(R.id.resourceRecycler);
        fstore=FirebaseFirestore.getInstance();
        fstore.collection("Resources").document("Subjects").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    resourceSubject=new ArrayList<>();
                    resourceSubject= (List<String>) value.get("subject");
                    setupRecycler();
                }
            }
        });



        return view;
    }

    private void setupRecycler() {

        adapter= new resourceAdapter(resourceSubject,this);
        resourceRecycler.setAdapter(adapter);
        resourceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        resourceRecycler.setHasFixedSize(true);
    }

    @Override
    public void onNoteClick(int position) {
        Intent i=new Intent(getActivity(),subjectResources.class);
        i.putExtra("Subject",resourceModels[position]);
        startActivity(i);
    }
}