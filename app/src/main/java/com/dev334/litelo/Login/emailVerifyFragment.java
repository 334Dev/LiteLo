package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


import com.dev334.litelo.R;
import com.google.android.material.snackbar.Snackbar;

public class emailVerifyFragment extends Fragment {

    private View view;
    private Button Done;
    private ConstraintLayout parentLayout;
    private static String TAG="EmailVerifyLOG";
    private ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_email_verification, container, false);

        Done=view.findViewById(R.id.verifcationDone);
        parentLayout=view.findViewById(R.id.verifyEmailLayout);
        loading=view.findViewById(R.id.VerificationLoading);

        loading.setVisibility(View.INVISIBLE);


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                SignInUser();
            }
        });

        return view;
    }

    private void SignInUser() {
        String Email=((LoginActivity)getActivity()).getSignUpEmail();
        String Password=((LoginActivity)getActivity()).getSignUpPassword();

    }
}