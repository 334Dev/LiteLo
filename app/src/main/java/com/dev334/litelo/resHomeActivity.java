package com.dev334.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class resHomeActivity extends AppCompatActivity {

    ResourcesFragment resourcesFragment;
    NotificationFragment notificationFragment;
    BottomNavigationView bottomNav;
    TextView activityName;
    private ImageView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_home);
        resourcesFragment=new ResourcesFragment();
        notificationFragment=new NotificationFragment();

        feedback=findViewById(R.id.menuMore);
        bottomNav= findViewById(R.id.bottomNavigationView);
        activityName=findViewById(R.id.activityName);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i=new Intent(resHomeActivity.this, userFeedback.class);
              startActivity(i);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,resourcesFragment).commit();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navi_resources:
                        feedback.setVisibility(View.VISIBLE);
                        activityName.setText("Resources");
                        replaceFragment(resourcesFragment);
                        break;
                    case R.id.navi_notification:
                        feedback.setVisibility(View.INVISIBLE);
                        activityName.setText("Upcoming Activities");
                        replaceFragment(notificationFragment);
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });
    }

    private void gotoMainActivity() {

        Intent intent = new Intent(resHomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
}