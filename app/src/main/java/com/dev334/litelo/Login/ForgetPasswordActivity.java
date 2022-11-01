package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.dev334.litelo.R;
import com.dev334.litelo.model.AuthResponse;
import com.dev334.litelo.model.LoginRequest;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private AppCompatButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setReferences();
        setListeners();
    }

    private void setReferences() {
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);
    }

    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    requestResetLink();
                else
                    email.requestFocus();
            }
        });
    }

    private boolean validate() {
        return !email.getText().toString().equals("");
    }

    private void requestResetLink() {
        submit.setEnabled(false);
        RetrofitAccessObject.getRetrofitAccessObject()
                .resetPassword(new LoginRequest(email.getText().toString(), ""))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            Intent i = new Intent(ForgetPasswordActivity.this, ConfirmationActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Invalid email", Toast.LENGTH_LONG).show();
                            submit.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(ForgetPasswordActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
                        submit.setEnabled(true);
                    }
                });
    }
}