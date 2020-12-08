package com.example.litelo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class phone_login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView editPhone, editOTP,state,logo_name,group;
    private LinearLayout emailLinear;
    private ImageView logo;
    private Button button;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mPhone;
    private View parentLayout;

    ProgressBar progressBar;
    CountryCodePicker codePicker;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        //transition Time period
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().setDuration(1000);
            getWindow().getSharedElementReturnTransition().setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator());
        }

        //declaring fields
        parentLayout = findViewById(android.R.id.content);
        editPhone=findViewById(R.id.editPhone);
        editOTP=findViewById(R.id.editOTP);
        button=findViewById(R.id.button);
        state=findViewById(R.id.state);
        codePicker=findViewById(R.id.textView_code);
        progressBar=findViewById(R.id.progressBar2);
        group=findViewById(R.id.groupname);
        emailLinear=findViewById(R.id.emailLinear);
        logo=findViewById(R.id.logo);
        logo_name=findViewById(R.id.logo_name);



        //FirebaseAuth initialize
        mAuth= FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //phone get number
                mPhone=editPhone.getText().toString();
                if(!verificationInProgress){
                    if(mPhone.length() == 10){

                        String phoneNo = "+"+codePicker.getSelectedCountryCode()+mPhone;
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(phoneNo);

                    }else
                    {
                        editPhone.setError("Phone number is not Valid");
                    }
                }else {
                    String userOTP = editOTP.getText().toString();

                    if(!userOTP.isEmpty() && userOTP.length() == 6){

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                        verifyAuth(credential);

                    }else {
                        editOTP.setError("Valid OTP is required");
                    }
                }
            }
        });

    }
    private void verifyAuth(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar.make(parentLayout, "Authentication Successful", Snackbar.LENGTH_SHORT).show();
                    Intent sharedintent= new Intent(phone_login.this, UserInfo.class);
                    //Transition Animation
                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(phone_login.this,
                            new android.util.Pair<View, String>(logo, "logoTransition"),
                            new android.util.Pair<View, String>(logo_name,"logoNameTransition"),
                            new android.util.Pair<View, String>(emailLinear,"emailLayoutTransition"),
                            //new Pair<View, String>(group, "groupLayoutTransition"),
                            new Pair<View, String>(button, "buttonTransition"),
                            new Pair<View, String>(state, "textTransition"),
                            new Pair<View, String>(editPhone, "emailInputTransition"),
                            new Pair<View, String>(editOTP, "passwordTransition"));
                    startActivity(sharedintent, options.toBundle());

                }else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(phone_login.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void requestOTP(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                state.setText("OTP sent");
                editOTP.setVisibility(View.VISIBLE);
                verificationId =  s;
                token = forceResendingToken;
                button.setText("Verify");
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(phone_login.this, "Cannot create acount" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}