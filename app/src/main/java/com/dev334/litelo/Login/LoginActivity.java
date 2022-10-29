package com.dev334.litelo.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.LoginRequest;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private EditText email, password;
    private AppCompatButton submit;
    private TextView registerHere;
    private ProgressBar loading;
    private String emailSubmitted;
    private HashMap<String, String> admins = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();
        setListeners();
        getAdmins();
    }

    private void getAdmins() {
        // TODO
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.ADMIN, true);
        editor.putString(Constants.ADMIN_OF, "0ade98ac-fde4-4eb2-b301-5dfc6dc04286");
        editor.putString(Constants.PARENT, "Cyberquest");
        editor.apply();
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
        emailSubmitted = emailText;
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
        LoginRequest requestBody = new LoginRequest(email.getText().toString(), password.getText().toString());
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
                        editor.putString(Constants.EMAIL, emailSubmitted);
                        editor.apply();
                        getNotificationSubscriptions();
                    } catch (Exception exception) {
                        showMessage("Some error occurred");
                    }
                } else {
                    showMessage("Incorrect credentials");
                    loading.setVisibility(View.GONE);
                    submit.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showMessage("Some error occurred");
                loading.setVisibility(View.GONE);
                submit.setEnabled(true);
            }
        });
    }

    private void getNotificationSubscriptions() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        FirebaseFirestore.getInstance()
                .collection(Constants.SUBSCRIPTIONS)
                .document(emailSubmitted)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            SharedPreferences.Editor editor = preferences.edit();
                            for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                                if ((Boolean) entry.getValue()) {
                                    editor.putBoolean(entry.getKey(), true);
                                }
                            }
                            editor.apply();
                        }
                        goToHome();
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