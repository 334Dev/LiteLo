package com.dev334.litelo.UI.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.AdminActivity;
import com.dev334.litelo.ChangePassword;
import com.dev334.litelo.Database.TinyDB;
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

    private LinearLayout logout, deleteAcc, changePass, feedback, about, share,admin;
    private FirebaseAuth mAuth;
    private static String TAG="SettingsFragmentLog";
    private TinyDB tinyDB;

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
        share=root.findViewById(R.id.settings_share);
        admin=root.findViewById(R.id.settings_admin);
        mAuth=FirebaseAuth.getInstance();
        tinyDB=new TinyDB(getActivity());

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

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Developed by Dev334",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                View view=getLayoutInflater().inflate(R.layout.contact_us_dailog,null);
                alert.setView(view);
                AlertDialog show=alert.show();
                alert.setCancelable(true);
                show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Download LiteLo App");
                i.putExtra(Intent.EXTRA_TEXT, "Download LiteLo App \n https://play.google.com/store/apps/details?id=com.dev334.litelo");
                startActivity(Intent.createChooser(i, "Share app"));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean admin=tinyDB.getBoolean("Admin");
                if(admin) {
                    Intent i = new Intent(getContext(), AdminActivity.class);
                    i.putExtra("Branch", tinyDB.getString("Branch"));
                    startActivity(i);
                }else{
                    Toast.makeText(getContext(), "Access Denied", Toast.LENGTH_SHORT).show();
                }
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
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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