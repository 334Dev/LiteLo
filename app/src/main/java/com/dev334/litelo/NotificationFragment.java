package com.dev334.litelo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements resourceAdapter.onNoteListener  {

    private View view;
    private RecyclerView clubRecycler;
    private FirebaseFirestore firestore;
    private List<clubModel> clubModels;
    private upcomingAdapter clubAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        clubRecycler=view.findViewById(R.id.notifactionRec);

        clubModels=new ArrayList<>();
        clubAdapter= new upcomingAdapter(clubModels);
        clubRecycler.setAdapter(clubAdapter);
        clubRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
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
        return view;
    }

    @Override
    public void onNoteClick(int position) {

    }
}