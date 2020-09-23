package com.example.litelo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserInfo extends AppCompatActivity {

    private TextView editName, editGroup, editReg;
    private Button button;
    private String Name, Group, Reg;
    private Integer iReg;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String UserUID;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().setDuration(1000);
            getWindow().getSharedElementReturnTransition().setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator());
        }

        //fireStore instance
        firestore=FirebaseFirestore.getInstance();

        //getting UserID FirebaseAuth
        mAuth=FirebaseAuth.getInstance();
        UserUID=mAuth.getCurrentUser().getUid();


        editGroup=findViewById(R.id.editGroup);
        editName=findViewById(R.id.editName);
        editReg=findViewById(R.id.regNo);
        parentLayout = findViewById(android.R.id.content);

        button=findViewById(R.id.button);


        //Groups
        final String[] groups= new String[]{"EC1", "EC2", "EC3", "EC4"};

        //OnClick button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting Text
                Name=editName.getText().toString();
                Group=editGroup.getText().toString();
                Reg=editReg.getText().toString();
                iReg=Integer.parseInt(editReg.getText().toString());

                //Check and Further
                if(Name.isEmpty()){
                    editName.setError("Write the Name");
                }
                else if(Reg.isEmpty()){
                    editReg.setError("Write Your Registration No.");
                }
                else if(Group.isEmpty()){
                    editGroup.setError("Write the Group Name");
                }
                else if(!Arrays.asList(groups).contains(Group)){
                    editGroup.setError("InValid Group Name");
                }

                else if(iReg>20170000 && iReg<20210000){

                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", Name);
                    user.put("Group", Group);
                    user.put("RegNo", Reg);
                    firestore.collection("Users").document(UserUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(parentLayout, "Welcome "+Name+"!", Snackbar.LENGTH_SHORT).show();
                            Intent i= new Intent(UserInfo.this, HomeActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(parentLayout, "Error occurred in connecting to server", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    editReg.setError("Registration ID invalid");
                }
            }
        });


    }
}