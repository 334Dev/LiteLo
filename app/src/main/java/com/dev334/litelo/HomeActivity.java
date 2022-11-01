package com.dev334.litelo;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dev334.litelo.Interfaces.PassDataInterface;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.UI.home.HomeFragment;
import com.dev334.litelo.UI.notification.NotificationFragment;
import com.dev334.litelo.UI.settings.SettingsFragment;
import com.dev334.litelo.UI.splash.SplashActivity;
import com.dev334.litelo.model.TimelineModel;
import com.dev334.litelo.utility.Constants;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PassDataInterface {

    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private NotificationFragment notificationFragment;
    private ChipNavigationBar bottomNavigation;
    private List<TimelineModel> Events, TomorrowEvents;
    private String TAG = "HomeActivity";
    private AppUpdateManager mAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        checkUpdate();
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();
        //notificationFragment=new NotificationFragment();
        notificationFragment = new NotificationFragment();
        bottomNavigation = findViewById(R.id.bottom_navigation_bar);
        mAppUpdateManager = AppUpdateManagerFactory.create(this);


        Events = new ArrayList<>();
        TomorrowEvents = new ArrayList<>();


        if (savedInstanceState == null) {
//            bottomNavigation.setVisibility(View.INVISIBLE);
            bottomNavigation.setItemSelected(R.id.nav_home, true);
            replaceFragment(homeFragment);
        }

        bottomNavigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_settings:
                        replaceFragment(settingsFragment);
                        break;
                    case R.id.nav_notification:
                        replaceFragment(notificationFragment);
                        break;
                    default:
                        replaceFragment(homeFragment);
                        break;
                }
            }
        });

        checkIntentAndRedirect();

    }

    private void checkUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, HomeActivity.this, 1);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkIntentAndRedirect() {
        String event = getIntent().getStringExtra(Constants.EVENT_FROM_NOTIFICATION);
        String dept = getIntent().getStringExtra(Constants.DEPT_FROM_NOTIFICATION);
        if (event == null || dept == null) return;
        Intent i = new Intent(this, EventActivity.class);
        i.putExtra(Constants.EVENT_FROM_NOTIFICATION, event);
        i.putExtra(Constants.DEPT_FROM_NOTIFICATION, dept);
        startActivity(i);
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
    public void PassTodayEvents(List<TimelineModel> Events) {
        this.Events = Events;
    }

    @Override
    public void PassTomorrowEvents(List<TimelineModel> Events) {
        TomorrowEvents = Events;
    }

    public List<TimelineModel> getEvents() {
        return Events;
    }

    public List<TimelineModel> getTomorrowEvents() {
        return TomorrowEvents;
    }

    public void openLoginActivity(int n) {
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        i.putExtra("FRAGMENT", n);
        startActivity(i);
        finish();
    }

    public void setHomeFragment() {
        bottomNavigation.setVisibility(View.VISIBLE);
        replaceFragment(homeFragment);
        Log.i(TAG, "setHomeFragment: " + Events);
    }
}