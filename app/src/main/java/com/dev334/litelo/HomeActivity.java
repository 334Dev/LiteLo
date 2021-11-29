package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dev334.litelo.UI.home.HomeFragment;
import com.dev334.litelo.UI.settings.SettingsFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private ChipNavigationBar bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment=new HomeFragment();
        settingsFragment=new SettingsFragment();
        bottomNavigation=findViewById(R.id.bottom_navigation_bar);

        if(savedInstanceState==null){
            bottomNavigation.setItemSelected(R.id.nav_home, true);
            replaceFragment(homeFragment);
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

}