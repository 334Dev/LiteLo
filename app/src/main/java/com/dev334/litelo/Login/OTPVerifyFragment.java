package com.dev334.litelo.Login;

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

import com.dev334.litelo.Database.TinyDB;

import com.dev334.litelo.R;
import com.google.android.material.snackbar.Snackbar;

public class OTPVerifyFragment extends Fragment {

    private View view;
    private TextView editOTP;
    private Button verify;
    private TinyDB tinyDB;
    private String PhoneNo, VerificationID;
    private ConstraintLayout parentLayout;
    private ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_o_t_p_verify, container, false);

        editOTP=view.findViewById(R.id.EditOTPTxt);
        verify=view.findViewById(R.id.VerifyOTP);
        tinyDB=new TinyDB(getContext());
        parentLayout=view.findViewById(R.id.OTPFragmentLayout);
        loading=view.findViewById(R.id.loadingOTP);

        loading.setVisibility(View.INVISIBLE);

        PhoneNo=((LoginActivity)getActivity()).getPhoneNo();
        VerificationID=((LoginActivity)getActivity()).getVerificationID();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP;
                OTP=editOTP.getText().toString();

                if(OTP.isEmpty()){
                    editOTP.setError("Enter valid OTP");
                }else{
                    loading.setVisibility(View.VISIBLE);

                }
            }
        });

        return view;
    }

}