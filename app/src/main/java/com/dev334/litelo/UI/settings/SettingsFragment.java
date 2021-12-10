package com.dev334.litelo.UI.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev334.litelo.ChangePassword;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.UserFeedback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private LinearLayout logout, deleteAcc, changePass, feedback, about;
    private FirebaseAuth mAuth;
    private static String TAG="SettingsFragmentLog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_settings, container, false);

        logout=root.findViewById(R.id.settings_logout);
        deleteAcc=root.findViewById(R.id.settings_delete);
        changePass=root.findViewById(R.id.settings_changePass);
        feedback=root.findViewById(R.id.settings_feedback);
        about=root.findViewById(R.id.settings_about);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser().getEmail().isEmpty()){
            changePass.setVisibility(View.GONE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout user and send them to login activity
                mAuth.signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAccount();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open feedback activity
                Intent i=new Intent(getActivity(), UserFeedback.class);
                startActivity(i);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change Password
                Intent i = new Intent(getActivity(), ChangePassword.class);
                startActivity(i);
            }
        });

        return root;
    }

    private void DeleteAccount() {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.dialog_delete_account,null);
        TextView delete=view.findViewById(R.id.dDeleteAcc);
        TextView cancel=view.findViewById(R.id.dCancel);
        alert.setView(view);
        AlertDialog show=alert.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i=new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //setSnackBar(scrollView, "Try After logging in again");
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}