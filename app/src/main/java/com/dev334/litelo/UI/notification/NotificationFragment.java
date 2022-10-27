package com.dev334.litelo.UI.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.ClubModel;
import com.dev334.litelo.UpcomingAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements UpcomingAdapter.onNoteListener {

    private View view;
    private RecyclerView clubRecycler;
    private FirebaseFirestore firestore;
    private List<ClubModel> clubModels;
    private UpcomingAdapter clubAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        clubRecycler=view.findViewById(R.id.notifactionRec);

        clubModels=new ArrayList<>();
        clubAdapter= new UpcomingAdapter(clubModels, this);
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
                        clubModels.add(snapshot.toObject(ClubModel.class));
                    }
                    clubAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    @Override
    public void onNoteClick(int position) {
        Log.i("recyclerViewOnClick", "recyclerviewOnClick: Clicked");
        String link=clubModels.get(position).getLink();
        if(link=="N/A"){
            Toast.makeText(getContext(),"No link provided by admin", Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(i);
        }
    }
}