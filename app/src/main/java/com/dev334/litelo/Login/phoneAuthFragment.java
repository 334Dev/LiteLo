package com.dev334.litelo.Login;

import static com.dev334.litelo.Login.signUpFragment.setSnackBar;

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


import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class phoneAuthFragment extends Fragment {

    private TextView editPhone, editCode,state;
    private Button Generate;
    private View view;
    private String code,phone;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private Boolean verificationInProgress=false;
    private String TAG="phoneAuthFragment";
    private FirebaseAuth mAuth;
    private ProgressBar loading;
    private ConstraintLayout parentLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_phone_auth_fagment, container, false);

        editCode=view.findViewById(R.id.countryCodePhone);
        editPhone=view.findViewById(R.id.PhoneNumber);
        state=view.findViewById(R.id.textView47);
        Generate=view.findViewById(R.id.GenerateOTPBtn);
        mAuth=FirebaseAuth.getInstance();
        loading=view.findViewById(R.id.phoneLoading);
        loading.setVisibility(View.INVISIBLE);

        parentLayout=view.findViewById(R.id.PhoneAuthLayout);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //not needed
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                loading.setVisibility(View.INVISIBLE);
                setSnackBar(parentLayout, "An error occurred");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                loading.setVisibility(View.INVISIBLE);
                ((LoginActivity)getActivity()).setPhoneNo(phone, mVerificationId, mResendToken);
                ((LoginActivity)getActivity()).openPhoneOTP();
            }
        };


        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Generate");
                code=editCode.getText().toString();
                phone=editPhone.getText().toString();

                if(code.isEmpty()){
                    editCode.setError("Enter Country Code");
                }else if(phone.isEmpty()){
                    editPhone.setError("Enter your phone number");
                }else{
                    loading.setVisibility(View.VISIBLE);
                    String phoneNo = code+phone;
                    startPhoneNumberVerification(phoneNo);
                }
            }
        });

        return view;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i(TAG, "startPhoneNumberVerification: Started verifying");
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

}