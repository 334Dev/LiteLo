package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dev334.litelo.Interfaces.PassDataInterface;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.UI.home.HomeFragment;
import com.dev334.litelo.UI.settings.SettingsFragment;
import com.dev334.litelo.UI.splashScreen.SplashFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements PassDataInterface {

    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private SplashFragment splashFragment;
    private ChipNavigationBar bottomNavigation;
    private List<EventModel> Events, TomorrowEvents;
    private String TAG="HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment=new HomeFragment();
        settingsFragment=new SettingsFragment();
        splashFragment=new SplashFragment(this);
        bottomNavigation=findViewById(R.id.bottom_navigation_bar);

        Events=new ArrayList<>();
        TomorrowEvents=new ArrayList<>();

        if(savedInstanceState==null){
            bottomNavigation.setVisibility(View.INVISIBLE);
            bottomNavigation.setItemSelected(R.id.nav_home, true);
            replaceFragment(splashFragment);
        }

        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.nav_settings:
                        replaceFragment(settingsFragment);
                        break;
                    case R.id.nav_profile:
                        replaceFragment(settingsFragment);
                        break;
                    default:
                        replaceFragment(homeFragment);
                        break;
                }
            }
        });

    }

    private void replaceFragment(Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hide all of the fragments
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }

        if (fragmentToShow.isAdded()) {
            // When fragment was previously added - show it
            transaction.show(fragmentToShow);
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.FragmentContainer, fragmentToShow);
        }

        transaction.commit();
    }

    @Override
    public void PassTodayEvents(List<EventModel> Events) {
        this.Events=Events;
    }

    @Override
    public void PassTomorrowEvents(List<EventModel> Events) {
        TomorrowEvents=Events;
    }

    public List<EventModel> getEvents(){
        return Events;
    }

    public List<EventModel> getTomorrowEvents(){
        return TomorrowEvents;
    }

    public void openLoginActivity(int n) {
        Intent i= new Intent(HomeActivity.this, LoginActivity.class);
        i.putExtra("FRAGMENT", n);
        startActivity(i);
        finish();
    }

    public void setHomeFragment() {
        bottomNavigation.setVisibility(View.VISIBLE);
        replaceFragment(homeFragment);
        Log.i(TAG, "setHomeFragment: "+Events);
    }
}