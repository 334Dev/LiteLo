package com.dev334.litelo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserInfo extends AppCompatActivity {

    private TextView editName, editGroup, editReg;
    private Button button, skipBtn;
    private String Name, Group, Reg;
    private Integer iReg;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String UserUID;
    private View parentLayout;
    private String[] groupsMech, groupsChem;
    private List<String> groupAllowed;
    private List<String> presentArray, absentArray, cancelArray;;

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
        skipBtn=findViewById(R.id.skipBtn);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserInfo.this, resHomeActivity.class);
                startActivity(i);
            }
        });

        //get allowed groups
        groupAllowed=new ArrayList<>();

        firestore.collection("TimeTable").document("group").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                groupAllowed= (List<String>) value.get("allowed");
            }
        });


        //Groups
        groupsMech= new String[]{"A1","A2","B1","B2","C1","C2","D1","D2","E1","E2"};
        groupsChem=new String[]{"F1","F2","G1","G2","H1","H2","J1","J2","I1","I2"};
        final String[] groups=new String[]{"A1","A2","B1","B2","C1","C2","D1","D2","E1","E2","F1","F2","G1","G2","H1","H2","J1","J2","I1","I2"};

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
                else if(!groupAllowed.contains(Group)){
                    //Snackbar.make(parentLayout, "Sorry our app is currently unavailable for your group, check back later.", Snackbar.LENGTH_SHORT).show();
                    AlertDialog.Builder alert=new AlertDialog.Builder(UserInfo.this);
                    View view=getLayoutInflater().inflate(R.layout.resource_dialog,null);
                    Button accessRes=view.findViewById(R.id.accessRes);
                    alert.setView(view);
                    accessRes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(UserInfo.this, resHomeActivity.class);
                            startActivity(i);
                        }
                    });
                    alert.setCancelable(true);
                    AlertDialog show=alert.show();
                    show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                }
                else if(iReg>20170000 && iReg<20210000){

                    SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(UserInfo.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Group_Name", Group);
                    editor.apply();

                    Calendar date= Calendar.getInstance();
                    final String deviceDay=date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());


                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", Name);
                    user.put("Group", Group);
                    user.put("RegNo", Reg);
                    user.put("Date", "lorem");

                    firestore.collection("Users").document(UserUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                addClasses();
                                Snackbar.make(parentLayout, "Welcome " + Name + "!", Snackbar.LENGTH_SHORT).show();
                                Intent i = new Intent(UserInfo.this, HomeActivity.class);
                                i.putExtra("Group_Name",Group);
                                startActivity(i);

                                Snackbar.make(parentLayout, "An error occurred", Snackbar.LENGTH_SHORT).show();

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

    private void addClasses() {
        //default present absent value
        final Map<String, Object> map= new HashMap<>();
        map.put("Present", 0);
        map.put("Absent", 0);
        map.put("absentStatus", false);
        map.put("presentStatus", false);


        presentArray=new ArrayList<>();
        presentArray.add("demouser");

        absentArray=new ArrayList<>();
        absentArray.add("demouser");

        cancelArray=new ArrayList<>();
        cancelArray.add("demouser");

        map.put("presentArray",presentArray);
        map.put("absentArray",absentArray);
        map.put("cancelArray",cancelArray);

        //integer for success
        final Integer[] success = new Integer[1];

        //sem check
        if(Arrays.asList(groupsMech).contains(editGroup.getText().toString())) {
            //subject list
            final String[] mechClasses={"Physics(L)","Physics(T)","Maths(L)","Maths(T)","ELC(L)","Mechanics(L)","Mechanics(T)",
                    "ELC(T)","Language(P)","Workshop(P)","Workshop(L)","Physics(P)","Mechanics(P)"};


            //creating document of each subject
            for(int i=0;i<mechClasses.length;i++) {
                map.put("Subject",mechClasses[i]);
                firestore.collection("Users").document(UserUID).collection("Classes")
                        .document(mechClasses[i]).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("All Subjects added", "onSuccess: Subject Added");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }else{
            //subject list
            final String[] chemClasses={"Physics(L)","Physics(T)","Maths(L)","Maths(T)","CS(L)","Chemistry(L)","Chemistry(T)",
                    "CS(T)","CSW(L)","ED(P)","ED(L)","CS(P)","Chemistry(P)"};


            //creating document of each subject
            for(int i=0;i<chemClasses.length;i++) {
                map.put("Subject",chemClasses[i]);
                firestore.collection("Users").document(UserUID).collection("Classes")
                        .document(chemClasses[i]).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("All Subjects added", "onSuccess: Subject Added");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }
    }
}