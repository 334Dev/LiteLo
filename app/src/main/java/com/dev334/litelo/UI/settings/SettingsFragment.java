package com.dev334.litelo.UI.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.AdminActivity;
import com.dev334.litelo.ChangePassword;
import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.UserFeedback;
import com.dev334.litelo.model.AdminModel;
import com.dev334.litelo.model.UserModel;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private LinearLayout logout, deleteAcc, changePass, feedback, share, admin;
    private TextView name,email,mobile_no;
    private SharedPreferences preferences;
    private static String TAG = "SettingsFragmentLog";
    private TinyDB tinyDB;
    RecyclerView teams_recycler;
    private UserModel userModel = new UserModel();

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
        name = root.findViewById(R.id.name_tv);
        email = root.findViewById(R.id.email_tv);
        mobile_no = root.findViewById(R.id.mobile_no);
//        getUserData();
//        setProfileValues();
        String adminOf = preferences.getString(Constants.ADMIN, "");
        Gson gson = new GsonBuilder().create();
        ArrayList<AdminModel> adminModels = gson.fromJson(adminOf, new TypeToken<ArrayList<AdminModel>>() {
        }.getType());
        if (adminModels != null && adminModels.size() > 0) {
            admin.setVisibility(View.VISIBLE);
        }

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

    private void setProfileValues() {
        if(userModel == null) return;
        name.setText(userModel.getName());
        email.setText(userModel.getEmail());
        mobile_no.setText(userModel.getMobile_no());
    }

    private void getUserData() {
        String token = preferences.getString(Constants.TOKEN, "");
        RetrofitAccessObject.getRetrofitAccessObject()
                .getUserData(token)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.code() == 200) {
                            try {
                                if (response.body() == null) throw new Exception("Unqualified response");
                                userModel.setName(response.body().getName());
                                userModel.setEmail(response.body().getEmail());
                                userModel.setMobile_no(response.body().getMobile_no());
                            } catch (Exception exception) {
//                                Log.d("Hello2",exception.toString());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
//                        Log.d("Hello3","Failed");
                    }
                });
    }
}
