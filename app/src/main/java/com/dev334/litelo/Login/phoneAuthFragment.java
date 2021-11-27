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

import java.util.concurrent.TimeUnit;

public class phoneAuthFragment extends Fragment {

    private TextView editPhone, editCode;
    private Button Generate;
    private View view;
    private String code,phone;

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

    }

}