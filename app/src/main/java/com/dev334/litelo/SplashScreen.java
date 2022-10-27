package com.dev334.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev334.litelo.Login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

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



        //handler to delay
        Handler mHandler= new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser()==null){
                    Intent i= new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else {

                    String UserID=mAuth.getCurrentUser().getUid();
                    FirebaseUser user=mAuth.getCurrentUser();

                    if(!user.getPhoneNumber().isEmpty()){
                        firestore.collection("NewUsers").document(UserID).
                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                                    i.putExtra("FRAGMENT", 2);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Check Details", "onFailure: " + e.getMessage());
                            }
                        });
                    }

                    else if(!user.isEmailVerified()){
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.putExtra("FRAGMENT", 1);
                        startActivity(i);
                        finish();
                    }
                    else {
                        firestore.collection("NewUsers").document(UserID).
                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                                    i.putExtra("FRAGMENT", 2);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Check Details", "onFailure: " + e.getMessage());
                            }
                        });
                    }
                }
            }
        },500);


    }
}