package com.dev334.litelo.UI.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.dev334.litelo.AdminActivity;
import com.dev334.litelo.ChangePassword;
import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.UserFeedback;
import com.dev334.litelo.model.AdminModel;
import com.dev334.litelo.utility.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private LinearLayout logout, deleteAcc, changePass, feedback, share, admin;
    private SharedPreferences preferences;
    private static String TAG = "SettingsFragmentLog";
    private TinyDB tinyDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        preferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        logout = root.findViewById(R.id.settings_logout);
        deleteAcc = root.findViewById(R.id.settings_delete);
        changePass = root.findViewById(R.id.settings_changePass);
        feedback = root.findViewById(R.id.settings_feedback);
        share = root.findViewById(R.id.settings_share);
        admin = root.findViewById(R.id.settings_admin);
        tinyDB = new TinyDB(getActivity());

        String adminOf = preferences.getString(Constants.ADMIN, "");
        Gson gson = new GsonBuilder().create();
        ArrayList<AdminModel> adminModels = gson.fromJson(adminOf, new TypeToken<ArrayList<AdminModel>>() {
        }.getType());
        if (adminModels.size() > 0) {
            admin.setVisibility(View.VISIBLE);
        }

        /*boolean EMAIL = false;
        for (UserInfo u : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (u.getProviderId().equals("password")) {
                EMAIL = true;
            }
        }

        if (!EMAIL) {
            changePass.setVisibility(View.GONE);
        }*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout user and send them to login activity
                preferences.edit().clear().apply();
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
                Intent i = new Intent(getActivity(), UserFeedback.class);
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
                Intent i = new Intent(getContext(), AdminActivity.class);
                i.putExtra("Branch", tinyDB.getString("Branch"));
                startActivity(i);
            }
        });

        return root;
    }

    private void DeleteAccount() {
        /*AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_delete_account, null);
        TextView delete = view.findViewById(R.id.dDeleteAcc);
        TextView cancel = view.findViewById(R.id.dCancel);
        alert.setView(view);
        AlertDialog show = alert.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //setSnackBar(scrollView, "Try After logging in again");
                        Log.i(TAG, "onFailure: " + e.getMessage());
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
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
    }
}