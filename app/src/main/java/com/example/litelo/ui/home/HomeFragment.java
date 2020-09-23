package com.example.litelo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.litelo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseFirestore firestore;
    private TextView className, className2, disc, disc2, timeDate, timeDate2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        firestore=FirebaseFirestore.getInstance();

        className=root.findViewById(R.id.className);
        disc=root.findViewById(R.id.desc);
        timeDate=root.findViewById(R.id.dateTime);

        className2=root.findViewById(R.id.className2);
        disc2=root.findViewById(R.id.desc2);
        timeDate2=root.findViewById(R.id.dateTime2);

        setCcClasses();



        return root;
    }

    private void setCcClasses() {
        firestore.collection("ClubClasses").document("CC").collection("Classes")
                .whereEqualTo("visibility", "show").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotsList= queryDocumentSnapshots.getDocuments();
                int i=1;
                for(DocumentSnapshot snapshot: snapshotsList){
                    if(i==1) {
                        className.setText(snapshot.getString("Topic"));
                        disc.setText(snapshot.getString("Description"));
                        timeDate.setText(snapshot.getString("Timing"));
                    }
                    else if(i==2){
                        className2.setText(snapshot.getString("Topic"));
                        disc2.setText(snapshot.getString("Description"));
                        timeDate2.setText(snapshot.getString("Timing"));
                    }
                    else{
                        break;
                    }
                    i=i+1;
                }
            }
        });
    }
}