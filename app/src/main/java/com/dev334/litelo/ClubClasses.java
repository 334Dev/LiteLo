package com.dev334.litelo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.services.Constants;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ClubClasses extends AppCompatActivity implements com.dev334.litelo.clubAdapter.ClickInterface {
    
    private RecyclerView clubRecycler;
    private FirebaseFirestore firestore;
    private List<clubModel> clubModels;
    private clubAdapter clubAdapter;
    Spinner SubSpinner;
    Button SubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_classes);
        
        clubRecycler=findViewById(R.id.clubRecycler);

        clubModels=new ArrayList<>();

        firestore=FirebaseFirestore.getInstance();


        SubSpinner=findViewById(R.id.SubSpinner);


        createNotificationChannel();

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        findViewById(R.id.SubButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String topic=SubSpinner.getSelectedItem().toString();
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                Toast.makeText(getApplicationContext(),"Subscribed",Toast.LENGTH_SHORT).show();
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

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel=new NotificationChannel(Constants.Channel_ID,Constants.Channel_ID,NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(Constants.Channel_DESCRIPTION);
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
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