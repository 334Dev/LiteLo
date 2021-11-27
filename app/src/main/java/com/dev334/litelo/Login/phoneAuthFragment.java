package com.dev334.litelo.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.dev334.litelo.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneAuthFragment extends Fragment {

    private TextView editPhone, editCode,state;
    private Button Generate;
    private View view;
    private String code,phone;

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private Boolean verificationInProgress=false;

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

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code=editCode.getText().toString();
                phone=editPhone.getText().toString();

                if(code.isEmpty()){
                    editCode.setError("Enter Country Code");
                }else if(phone.isEmpty()){
                    editPhone.setError("Enter your phone number");
                }else{
                    String phoneNo = code+phone;
                    requestOTP(phoneNo);
                }
            }
        });

        return view;
    }

    private void requestOTP(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60L, TimeUnit.SECONDS, getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                state.setText("OTP sent");
                editCode.setVisibility(View.VISIBLE);
                verificationId =  s;
                token = forceResendingToken;
                Generate.setText("Verify");
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

                Toast.makeText(getContext(), "Cannot create acount" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}