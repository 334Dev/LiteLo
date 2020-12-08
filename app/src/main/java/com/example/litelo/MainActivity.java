package com.example.litelo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private TextView logo_name;
    private LinearLayout emailLinear;
    private ImageView logo;
    private Button email, google,phone;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private View parentLayout;
    private int RC_SIGN_IN=101;

    private static final String EMAIL = "email";
    private Intent i;
    //private ActivityFacebookBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        parentLayout = findViewById(android.R.id.content);

        //Logo and LogoName;
        logo=findViewById(R.id.logo);
        logo_name=findViewById(R.id.logo_name);

        //button
        email=findViewById(R.id.mobile);
        google=findViewById(R.id.google);
        phone=findViewById(R.id.phone);

        //Linear Layout
        emailLinear=findViewById(R.id.emailLinear);

        //firebase get instance
        mAuth = FirebaseAuth.getInstance();


        //onClick Email
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedintent= new Intent(MainActivity.this, email_Login.class);
                //Transition Animation
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        new android.util.Pair<View, String>(logo, "logoTransition"),
                        new android.util.Pair<View, String>(logo_name,"logoNameTransition"),
                        new android.util.Pair<View, String>(emailLinear,"emailLayoutTransition"));
                startActivity(sharedintent, options.toBundle());

            }
        });

        //phone login
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedintent= new Intent(MainActivity.this, phone_login.class);
                //Transition Animation
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        new android.util.Pair<View, String>(logo, "logoTransition"),
                        new android.util.Pair<View, String>(logo_name,"logoNameTransition"),
                        new android.util.Pair<View, String>(emailLinear,"emailLayoutTransition"));
                startActivity(sharedintent, options.toBundle());
            }
        });



        //googleLogin

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }




    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Snackbar.make(parentLayout, "Google Sign in Failed", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String UserUID=mAuth.getCurrentUser().getUid();
                            firestore=FirebaseFirestore.getInstance();
                            firestore.collection("Users").document(UserUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot snapshot=task.getResult();
                                    if(snapshot.exists()){
                                        Intent i= new Intent(MainActivity.this, splashScreen.class);
                                        startActivity(i);
                                    }
                                    else{
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Snackbar.make(parentLayout, "Authentication Successful", Snackbar.LENGTH_SHORT).show();
                                        Intent sharedintent= new Intent(MainActivity.this, UserInfo.class);
                                        //Transition Animation
                                        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                                                new android.util.Pair<View, String>(logo, "logoTransition"),
                                                new android.util.Pair<View, String>(logo_name,"logoNameTransition"),
                                                new android.util.Pair<View, String>(emailLinear,"emailLayoutTransition"));
                                        startActivity(sharedintent, options.toBundle());
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(parentLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}