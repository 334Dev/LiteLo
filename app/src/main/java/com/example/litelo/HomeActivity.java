package com.example.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private TextView headerName, headerReg;
    private ImageView headerPic;
    private FirebaseFirestore firestore;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //checking whether login or not
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser().getUid().isEmpty()){
            Intent i= new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
        }
        else{
           UserID=mAuth.getCurrentUser().getUid();
        }
        //Firestore instance
        firestore=FirebaseFirestore.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Inflate the header view
        View headerView= navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerName=headerView.findViewById(R.id.header_Name);
        headerReg=headerView.findViewById(R.id.branch_Reg);
        headerPic=headerView.findViewById(R.id.header_Pic);

        headerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent i= new Intent(HomeActivity.this,profileActivity.class);
                startActivity(i);

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
                String name=documentSnapshot.getString("Name");
                String group=documentSnapshot.getString("Group");
                String reg=documentSnapshot.getString("RegNo");

                headerName.setText(name);
                String regtxt=group+", "+reg;
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

                Toast.makeText(getApplicationContext(),"Nahi bana hai",Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Toast.makeText(getApplicationContext(),"Nahi bana hai",Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                mAuth.signOut();
                gotoMainActivity();

                return false;
            }
        });

        return true;
    }

    private void gotoMainActivity()
    {
        Intent intent= new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}