package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.Database.TinyDB;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.splashScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.abt.FirebaseABTesting;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class loginFragment extends Fragment {

    private View view;
    private Button Login, GoogleSignUp;
    private TextView EditEmail, EditPassword;
    private TextView ForgotPwd, NewUser;
    private String Email,Password;
    private int RC_SIGN_IN=101;
    private ProgressBar loading;
    private ConstraintLayout parentLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TinyDB tinyDB;
    private Map<String, Object> map;
    ArrayList<String> interest;
    private static String TAG="LoginFragmentLog";

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        tinyDB=new TinyDB(getContext());
        map=new HashMap<>();

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        GoogleSignUp=view.findViewById(R.id.GoogleSignUpLogin);
        ForgotPwd=view.findViewById(R.id.ForgetPasswordLogin);
        NewUser=view.findViewById(R.id.LoginTextSignup);
        EditEmail= view.findViewById(R.id.editEmailLogin);
        EditPassword=view.findViewById(R.id.editPasswordLogin);
        loading=view.findViewById(R.id.loginLoading);
        interest=new ArrayList<>();

        loading.setVisibility(View.INVISIBLE);

        Login=view.findViewById(R.id.btnLogin);

        parentLayout=view.findViewById(R.id.LoginFragmentLayout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignIn();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email=EditEmail.getText().toString();
                Password=EditPassword.getText().toString();
                if(check(Email,Password)) {
                    SignInUser();
                }else {
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });

        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LoginActivity)getActivity()).openSignup();
            }
        });

        ForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EditEmail.getText().toString().isEmpty()) {
                    EditEmail.setError("Enter the email ID");
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(EditEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Email sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed! verify email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    public static void setSnackBar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void GoogleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void SignInUser() {

        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        if(user.isEmailVerified()){
                            checkProfileCreated();
                        }else{
                            //email verify fragment
                            ((LoginActivity)getActivity()).openVerifyEmail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkProfileCreated() {
        String UserUID=mAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(UserUID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            startActivity(i);
                        }else{
                            ((LoginActivity)getActivity()).openCreateProfile();
                        }
                    }
                });
    }

    private boolean check(String email, String password) {
        if(email.isEmpty()){
            EditEmail.setError("Email is empty");
            return false;
        }else if(password.isEmpty()){
            EditPassword.setError("Password is empty");
            return  false;
        }else if(email.endsWith("@mnnit.ac.in")){
            EditEmail.setError("Use college Gsuite ID");
            return false;
        }
        else {
            if (password.length() < 6) {
                EditPassword.setError("Password is too short");
                return false;
            } else {
                return true;
            }
        }
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
                Log.i("GoogleFail", "onActivityResult: "+e.getMessage());
                // Google Sign In failed, update UI appropriately
                Snackbar.make(parentLayout, "Google Sign in Failed", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            checkGoogleEmail();
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(parentLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void checkGoogleEmail(){
        Email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(!Email.endsWith("@mnnit.ac.in")){
            mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Use college Gsuit ID", Toast.LENGTH_SHORT);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error Signing in", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            checkProfileCreated();
        }
    }

}