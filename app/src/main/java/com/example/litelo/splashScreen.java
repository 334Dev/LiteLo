package com.example.litelo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class splashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String group, UserID;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //overriding activity transition
        overridePendingTransition(R.anim.fadein, R.anim.splashscreen);
        mAuth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        firestore.setFirestoreSettings(settings);

        UserID=mAuth.getCurrentUser().getUid();

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        group = sharedPref.getString(getString(R.string.group_name), "N/A");

        if(group=="N/A") {
            Log.i("SharedPref_group", "onCreateView: couldn't find Group");
            firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    group = documentSnapshot.getString("Group");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.group_name), group);
                    editor.apply();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("GetGroupS", "onFailure: Failed");
                }
            });
            Log.i("GetGroupS", "onCreateView: " + group);
        }

        //handler to delay
        Handler mHandler= new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser()==null){
                    Intent i= new Intent(splashScreen.this, MainActivity.class);
                    startActivity(i);
                }
                else {

                    firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                Intent i = new Intent(splashScreen.this, HomeActivity.class);
                                startActivity(i);
                            }else{
                                Intent i = new Intent(splashScreen.this, UserInfo.class);
                                startActivity(i);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Check Details", "onFailure: "+e.getMessage());
                        }
                    });

                }
            }
        },500);


    }
}