package com.dev334.litelo.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.model.AdminModel;
import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.Coordinator;
import com.dev334.litelo.model.EventCoordinatorResponse;
import com.dev334.litelo.model.LoginRequest;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private SharedPreferences preferences;
    private String token;
    private final List<AdminModel> adminModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();
        setListeners();
    }

    private void getAdmins() {
        RetrofitAccessObject.getRetrofitAccessObject()
                .getEventsCoordinated(token)
                .enqueue(new Callback<EventCoordinatorResponse>() {
                    @Override
                    public void onResponse(Call<EventCoordinatorResponse> call, Response<EventCoordinatorResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Coordinator coordinator : response.body().getCoordieEvent()) {
                                AdminModel model = new AdminModel();
                                model.setEventId(coordinator.getEventId());
                                model.setEvent(coordinator.getEvent().getName());
                                model.setDeptId(coordinator.getEvent().getDeptEventId());
                                adminModels.add(model);
                            }
                            Gson gson = new GsonBuilder().create();
                            preferences.edit().putString(Constants.ADMIN, gson.toJson(adminModels)).apply();
                            goToHome();
                        } else {
                            retry("Some error occurred");
                        }
                    }

                    @Override
                    public void onFailure(Call<EventCoordinatorResponse> call, Throwable t) {
                        retry("Some error occurred");
                    }
                });
    }

    private void setReferences() {
        parent = findViewById(R.id.parent);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        registerHere = findViewById(R.id.registerHere);
        loading = findViewById(R.id.loginLoading);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
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
                retry("Some error occurred");
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
                    try {
                        SharedPreferences.Editor editor = preferences.edit();
                        if (response.body() == null || response.body().getToken().equals(""))
                            throw new Exception("Unqualified response");
                        token = response.body().getToken();
                        editor.putString(Constants.TOKEN, response.body().getToken());
                        editor.putString(Constants.EMAIL, emailSubmitted);
                        editor.apply();
                        getNotificationSubscriptions();
                    } catch (Exception exception) {
                        retry("Some error occurred");
                        loading.setVisibility(View.GONE);
                        submit.setEnabled(true);
                    }
                } else {
                    retry("Incorrect credentials");
                    loading.setVisibility(View.GONE);
                    submit.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                retry("Some error occurred");
                loading.setVisibility(View.GONE);
                submit.setEnabled(true);
            }
        });
    }

    private void getNotificationSubscriptions() {
        FirebaseFirestore.getInstance()
                .collection(Constants.SUBSCRIPTIONS)
                .document(emailSubmitted)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            SharedPreferences.Editor editor = preferences.edit();
                            if (task.getResult().getData() != null)
                                for (Map.Entry<String, Object> entry : task.getResult().getData().entrySet()) {
                                    if ((Boolean) entry.getValue()) {
                                        editor.putBoolean(entry.getKey(), true);
                                    }
                                }
                            editor.apply();
                            getAdmins();
                        } else {
                            getAdmins();
                        }
                    }
                });
    }

    private void retry(String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG).show();
        loading.setVisibility(View.GONE);
        submit.setEnabled(true);
        preferences.edit().clear().apply();
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}