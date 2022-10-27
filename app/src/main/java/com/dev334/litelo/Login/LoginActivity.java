package com.dev334.litelo.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.LoginData;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.PhoneAuthProvider;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private EditText email, password;
    private AppCompatButton submit;
    private TextView registerHere;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();
        setListeners();
    }

    private void setReferences() {
        parent = findViewById(R.id.parent);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        registerHere = findViewById(R.id.registerHere);
        loading = findViewById(R.id.loginLoading);
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.AVISHKAR_URL));
                startActivity(intent);
            }
        });
    }

    private void login() {
        if (validate()) {
            try {
                sendLoginRequest();
            } catch (JSONException exception) {
                showMessage("Some error occurred");
            }
        }
    }

    private boolean validate() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if (emailText.equals("")) {
            email.requestFocus();
            return false;
        }
        if (passwordText.equals("")) {
            password.requestFocus();
            return false;
        }
        return true;
    }

    private void sendLoginRequest() throws JSONException {
        loading.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        LoginData requestBody = new LoginData(email.getText().toString(), password.getText().toString());
        RetrofitAccessObject.getRetrofitAccessObject().signIn(requestBody).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.i("RequestBody", call.request().url().toString() + " " + response.toString() + "\n" + requestBody.toString());
                if (response.code() == 200) {
                    SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                    try {
                        SharedPreferences.Editor editor = preferences.edit();
                        if (response.body() == null || response.body().getToken().equals(""))
                            throw new Exception("Unqualified response");
                        editor.putString(Constants.TOKEN, response.body().getToken());
                        editor.apply();
                        goToHome();
                    } catch (Exception exception) {
                        showMessage("Some error occurred");
                    }
                } else {
                    showMessage("Incorrect credentials");
                }
                loading.setVisibility(View.GONE);
                submit.setEnabled(true);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showMessage("Some error occurred");
                loading.setVisibility(View.GONE);
                submit.setEnabled(true);
            }
        });
    }

    private void showMessage(String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}