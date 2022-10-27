package com.dev334.litelo.UI.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.utility.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.parent).setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        String token = preferences.getString(Constants.TOKEN, "");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (token.equals(""))
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                else
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}