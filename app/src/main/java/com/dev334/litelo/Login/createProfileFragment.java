package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.Database.TinyDB;


import java.util.ArrayList;

public class createProfileFragment extends Fragment {

    private View view;
    private TinyDB tinyDB;
    private String PhoneNo,Username,Organisation,Facebook, Instagram;
    private TextView EditName,EditFB,EditInsta,EditInterest;
    private TextView EditOrg;
    private Button btnCreate;
    private ArrayList<String> Organisations,userInterest;
    private static String TAG="CreateProfile";
    private String uInterest;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        EditName=view.findViewById(R.id.EditName);
        EditOrg=view.findViewById(R.id.EditOrg);
        EditFB=view.findViewById(R.id.EditFacebook);
        EditInsta=view.findViewById(R.id.EditInsta);
        btnCreate=view.findViewById(R.id.btnCreate);
        EditInterest=view.findViewById(R.id.EditInterest);


        Organisations=new ArrayList<>();
        userInterest=new ArrayList<>();
        tinyDB=new TinyDB(getContext());
        userInterest=tinyDB.getListString("UserInterest");
        setupText();
        PhoneNo=tinyDB.getString("PhoneNumber");

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Organisations);

        EditOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        EditInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username=EditName.getText().toString();
                Organisation=EditOrg.getText().toString();
                Facebook=EditFB.getText().toString();
                Instagram=EditInsta.getText().toString();

                if(check()){
                    ((LoginActivity)getActivity()).setProfileData(Username, Organisation, Facebook, Instagram, userInterest);
                    ((LoginActivity)getActivity()).openPhoneAuth();
                }
            }
        });
        
        return view;
    }

    private void setupText() {

        uInterest="";
        for(int i=0;i<userInterest.size();i++){
            uInterest=uInterest+userInterest.get(i)+" | ";
        }
        EditInterest.setText(uInterest);
    }



    private boolean check() {
        if(Username.isEmpty()){
            return false;
        }else if(Organisation.isEmpty()){
            return false;
        }else{
            if(Organisations.contains(Organisation)){
                if(!Instagram.isEmpty()){
                    if(!URLUtil.isValidUrl(Instagram)){
                        EditInsta.setError("Enter valid URL or else leave empty");
                        return false;
                    }else{
                        if(!Instagram.contains("instagram") || !Instagram.toLowerCase().contains("instagram")){
                            EditInsta.setError("Enter valid URL or else leave empty");
                            return false;
                        }
                    }
                }
                if(!Facebook.isEmpty()){
                    if(!URLUtil.isValidUrl(Facebook)){
                        EditFB.setError("Enter valid URL or else leave empty");
                        return false;
                    }else{
                        if(!Facebook.contains("facebook") || !Facebook.toLowerCase().contains("facebook")){
                            EditFB.setError("Enter valid URL or else leave empty");
                            return false;
                        }
                    }
                }
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EditOrg.setText(tinyDB.getString("Organisation"));
        userInterest=tinyDB.getListString("UserInterest");
        setupText();
    }
    
}