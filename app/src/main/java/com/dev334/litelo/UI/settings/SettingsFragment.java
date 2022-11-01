package com.dev334.litelo.UI.settings;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev334.litelo.AdminActivity;
import com.dev334.litelo.ChangePassword;
import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.UI.notification.NotificationAdapter;
import com.dev334.litelo.UserFeedback;
import com.dev334.litelo.model.AdminModel;
import com.dev334.litelo.model.Participation;
import com.dev334.litelo.model.Team;
import com.dev334.litelo.model.TeamInvitesResponse;
import com.dev334.litelo.model.UserDetails;
import com.dev334.litelo.model.UserResponse;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private LinearLayout logout, deleteAcc, changePass, feedback, share, admin;
    private TextView name, email, mobile_no, viewTeams;
    private SharedPreferences preferences;
    private static String TAG = "SettingsFragmentLog";
    private TinyDB tinyDB;
    private UserDetails details;
    private List<Team> teams = new ArrayList<>();
    private RecyclerView teamsRecyclerView;
    private TeamsAdapter teamsAdapter;

    @SuppressLint("MissingInflatedId")
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
        viewTeams = root.findViewById(R.id.view_teams);
        getUserData();

        teamsRecyclerView = root.findViewById(R.id.teams_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setAutoMeasureEnabled(false);
        teamsRecyclerView.setLayoutManager(llm);
        getTeams();

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

        viewTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(teamsRecyclerView.getVisibility() == View.VISIBLE){
                     teamsRecyclerView.setVisibility(View.GONE);
                 }else{
                     teamsRecyclerView.setVisibility(View.VISIBLE);
                 }
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
        if (details == null) return;
        name.setText(details.getName());
        email.setText(details.getEmail());
        mobile_no.setText(details.getMobile());
    }

    private void getUserData() {
        String token = preferences.getString(Constants.TOKEN, "");
        RetrofitAccessObject.getRetrofitAccessObject()
                .getUserData(token)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            details = response.body().getDetails();
                            setProfileValues();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {

                    }
                });
    }

    private void setTeamsData(){
        if(teams == null){
            Toast.makeText(getContext(), "No teams found!!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getTeams() {
        RetrofitAccessObject.getRetrofitAccessObject()
                .getTeamInvites(preferences.getString(Constants.TOKEN, ""))
                .enqueue(new Callback<TeamInvitesResponse>() {
                    @Override
                    public void onResponse(Call<TeamInvitesResponse> call, Response<TeamInvitesResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                            teams = response.body().getTeams();
                        }
                        teamsAdapter = new TeamsAdapter(teams, requireContext());
                        teamsRecyclerView.setAdapter(teamsAdapter);
                    }

                    @Override
                    public void onFailure(Call<TeamInvitesResponse> call, Throwable t) {

                    }
                });
    }



}
