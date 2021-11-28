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
import static com.dev334.litelo.Login.signUpFragment.setSnackBar;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.Database.TinyDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createProfileFragment extends Fragment {

    private View view;
    private TinyDB tinyDB;
    private String FullName,RegNo;
    private TextView EditName,EditReg;
    private Button btnCreate;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String UserUID;
    private View parentLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        parentLayout = view.findViewById(android.R.id.content);
        EditName=view.findViewById(R.id.EditName);
        EditReg=view.findViewById(R.id.EditReg);
        btnCreate=view.findViewById(R.id.btnCreate);
        mAuth=FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UserUID = mAuth.getUid();
        tinyDB=new TinyDB(getContext());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullName=EditName.getText().toString();
                RegNo=EditReg.getText().toString();

                if(FullName.isEmpty()){
                    EditName.setError("Write the Name");
                }
                else if(RegNo.isEmpty()){
                    EditReg.setError("Write Your Registration No.");
                }
                else if(Integer.parseInt(RegNo) < 20170000 || Integer.parseInt(RegNo) > 20220000){
                    EditReg.setError("Invalid Registration No.");
                }
                else{
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", FullName);
                    user.put("Group", RegNo);

                    firestore.collection("NewUsers").document(UserUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            setSnackBar(parentLayout, "Welcome " + FullName + "!");
                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);

                            //.make(parentLayout, "An error occurred", Snackbar.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setSnackBar(parentLayout, "An error occurred");
                        }
                    });
                }
            }
        });
        
        return view;
    }

    
}