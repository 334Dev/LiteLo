package com.example.litelo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class email_Login extends AppCompatActivity {

    private LinearLayout emailLinear;
    private TextView editMail, editPassword, textLogin, logo_name,group,forgotPass;
    private ImageView logo;
    private FirebaseAuth mAuth;
    private String email,password;
    private Integer userStatus=0;
    private Button button;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__login);
        //transition Time period
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().setDuration(1000);
            getWindow().getSharedElementReturnTransition().setDuration(1000)
                    .setInterpolator(new DecelerateInterpolator());
        }

        firestore=FirebaseFirestore.getInstance();

        emailLinear=findViewById(R.id.emailLinear);
        editMail=findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPassword);
        textLogin=findViewById(R.id.state);
        button=findViewById(R.id.button);
        group=findViewById(R.id.groupname);
        forgotPass=findViewById(R.id.forgotPass);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMail.getText().toString().isEmpty()) {
                    editMail.setError("Enter the email ID");
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(editMail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed! verify email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        logo=findViewById(R.id.logo);
        logo_name=findViewById(R.id.logo_name);


        //hint color
        editPassword.setHintTextColor(getColor(R.color.colorWhite));
        editMail.setHintTextColor(getColor(R.color.colorWhite));

        //FirebaseAuth initialize
        mAuth=FirebaseAuth.getInstance();

        //button On click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting Email and Password
                email=editMail.getText().toString();
                password=editPassword.getText().toString();

                //check if email and password are not empty
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fill All the Credentials", Toast.LENGTH_LONG).show();
                }

                else {
                    //userStatus
                    if (userStatus == 0) {
                        RegisterNewUser();
                    } else {
                        LoginUser();
                    }
                }

            }
        });


        //textLogin
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userStatus==0) {
                    //Register -> Login
                    button.setText("Login");
                    textLogin.setText("New Here? Create Account");
                    userStatus=1;
                }
                else{
                    //Login -> Register
                    button.setText("Register");
                    textLogin.setText("Already registered? Login here");
                    userStatus=0;
                }
            }
        });



    }

    private void LoginUser() {
        //Login Already Existing User
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //On successful Login
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                            String UserUID=mAuth.getCurrentUser().getUid();
                            firestore.collection("Users").document(UserUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot snapshot=task.getResult();
                                    if(snapshot.exists()){
                                        Intent i= new Intent(email_Login.this, splashScreen.class);
                                        startActivity(i);
                                    }
                                    else{
                                        Intent i= new Intent(email_Login.this, UserInfo.class);
                                        startActivity(i);
                                    }
                                }
                            });

                        }
                        else{
                            //on Failed Login
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void RegisterNewUser() {
        //SignUp new user
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //onSuccessful signUp
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                            Intent sharedintent= new Intent(email_Login.this, UserInfo.class);
                            //Transition Animation
                            ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(email_Login.this,
                                    new android.util.Pair<View, String>(logo, "logoTransition"),
                                    new android.util.Pair<View, String>(logo_name,"logoNameTransition"),
                                    new android.util.Pair<View, String>(emailLinear,"emailLayoutTransition"),
                                    new Pair<View, String>(group, "groupLayoutTransition"),
                                    new Pair<View, String>(button, "buttonTransition"),
                                    new Pair<View, String>(textLogin, "textTransition"),
                                    new Pair<View, String>(editMail, "emailInputTransition"),
                                    new Pair<View, String>(editPassword, "passwordTransition"));
                            startActivity(sharedintent, options.toBundle());
                        }
                        else{
                            // OnFailure
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}