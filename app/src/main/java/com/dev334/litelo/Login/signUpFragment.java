package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.Database.TinyDB;

import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class signUpFragment extends Fragment {

    private View view;
    private Button SignUp, PhoneSignUp;
    private TextView EditEmail, EditPassword, Login;
    private String Email,Password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressBar loading;
    private ConstraintLayout parentLayout;
    private String TAG="SignUpFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        PhoneSignUp=view.findViewById(R.id.phoneAuthBtn);

        EditEmail= view.findViewById(R.id.editEmailSignup);
        EditPassword=view.findViewById(R.id.editPasswordSignUp);
        loading=view.findViewById(R.id.SignUpLoading);
        Login=view.findViewById(R.id.LoginTextSignup);

        mAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).openLogin();
            }
        });

        PhoneSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).openPhoneAuth();
            }
        });

        loading.setVisibility(View.INVISIBLE);
        SignUp=view.findViewById(R.id.SignUpButton);

        parentLayout=view.findViewById(R.id.signUpFragmentLayout);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email=EditEmail.getText().toString();
                Password=EditPassword.getText().toString();
                if(check(Email,Password)){
                    signUpUser();
                }
            }
        });

        return view;
    }


    public static void setSnackBar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void signUpUser(){

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendVerificationEmail();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setSnackBar(parentLayout, "Signup failed");
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void sendVerificationEmail(){
        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                setSnackBar(parentLayout, "Email Sent");
                ((LoginActivity)getActivity()).openVerifyEmail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setSnackBar(parentLayout,"Failed to send verification email");
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private boolean check(String email, String password) {
        if(email.isEmpty()){
            EditEmail.setError("Email is empty");
            return false;
        }else if(password.isEmpty()){
            EditPassword.setError("Password is empty");
            return  false;
        }else {
            if (password.length() < 6) {
                EditPassword.setError("Password is too short");
                return false;
            } else {
                return true;
            }
        }
    }

}