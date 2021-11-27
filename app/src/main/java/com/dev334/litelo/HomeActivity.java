package com.dev334.litelo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.services.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;



public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private TextView headerName, headerReg, ViewProfile;
    private CircleImageView headerPic;
    private FirebaseFirestore firestore;
    private String UserID;
    private StorageReference storageReference;
    public static boolean soundState;  //sound
    private ImageView instagram, shareApp,reviewButton;

    ReviewManager manager;
    ReviewInfo reviewInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel=new NotificationChannel(Constants.Channel_ID,Constants.Channel_ID,NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(Constants.Channel_DESCRIPTION);
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mNotificationManager.createNotificationChannel(mChannel);
        }


        //checking whether login or not
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser().getUid().isEmpty()) {
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            UserID = mAuth.getCurrentUser().getUid();
        }
        //Firestore instance
        firestore = FirebaseFirestore.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        instagram=navigationView.findViewById(R.id.instagram);
        shareApp=navigationView.findViewById(R.id.shareApp);
        reviewButton=navigationView.findViewById(R.id.reviewButton);

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager= ReviewManagerFactory.create(HomeActivity.this);
                Task<ReviewInfo> request=manager.requestReviewFlow();


                request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(@NonNull Task<ReviewInfo> task) {
                        if(task.isSuccessful())
                        {
                            reviewInfo=task.getResult();
                            Task<Void> flow=manager.launchReviewFlow(HomeActivity.this,reviewInfo);
                            flow.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void result) {

                                }
                            });

                        }else
                        {
                            Toast.makeText(HomeActivity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://instagram.com/litelo.334?igshid=1rx9e57doj5pp"));
                startActivity(i);
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Download LiteLo App");
                i.putExtra(Intent.EXTRA_TEXT, "Download LiteLo App \n https://play.google.com/store/apps/details?id=com.dev334.litelo");
                startActivity(Intent.createChooser(i, "Share app"));
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                Uri pdfUri = Uri.parse("https://play.google.com/store/apps/details?id=com.dev334.litelo");
//                sharingIntent.setType("*/*");
//                sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
//                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        //Inflate the header view
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerName = headerView.findViewById(R.id.header_Name);
        headerReg = headerView.findViewById(R.id.branch_Reg);
        headerPic = headerView.findViewById(R.id.header_Pic);
        ViewProfile = headerView.findViewById(R.id.viewProfile);


        ViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this, profileActivity.class);
                startActivity(i);

            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(headerPic);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Profile Img Chcg", "onFailure: dsgfdhgfnhgmhgm");
            }
        });


        //headerView Text
        setHeaderViewText();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_subjectlist, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    private void setHeaderViewText() {
        //Taking User Data from firebaseFirestore

        firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("Name");
                String group = documentSnapshot.getString("Group");
                String reg = documentSnapshot.getString("RegNo");

                headerName.setText(name);
                String regtxt = group + ", " + reg;
                headerReg.setText(regtxt);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i=new Intent(HomeActivity.this, userFeedback.class);
                startActivity(i);
                return false;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                mAuth.signOut();
                gotoMainActivity();

                return false;
            }
        });

        return true;
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}