package com.dev334.litelo.Login;

import static com.dev334.litelo.Login.signUpFragment.setSnackBar;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class loginFragment extends Fragment {

    private View view;
    private Button Login, phoneAuth;
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

        phoneAuth=view.findViewById(R.id.phoneAuthBtn);
        ForgotPwd=view.findViewById(R.id.ForgetPasswordLogin);
        NewUser=view.findViewById(R.id.LoginTextSignup);
        EditEmail= view.findViewById(R.id.editEmailLogin);
        EditPassword=view.findViewById(R.id.editPasswordLogin);
        loading=view.findViewById(R.id.loginLoading);
        interest=new ArrayList<>();

        loading.setVisibility(View.INVISIBLE);

        Login=view.findViewById(R.id.btnLogin);

        parentLayout=view.findViewById(R.id.LoginFragmentLayout);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email=EditEmail.getText().toString();
                Password=EditPassword.getText().toString();
                if(check(Email,Password)) {
                    isAdmin(Email);
                }else {
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });

        phoneAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).openPhoneAuth();
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
    private void isAdmin(String email) {
        firestore.collection("Admin").whereEqualTo("Email", email)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs=queryDocumentSnapshots.getDocuments();
                if(docs.isEmpty()){
                    tinyDB.putBoolean("Admin", false);
                }else{
                    tinyDB.putBoolean("Admin", true);
                    tinyDB.putString("Branch", docs.get(0).get("Branch").toString());
                }
                SignInUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tinyDB.putBoolean("Admin", false);
                Log.i(TAG, "onFailure: "+e.getMessage());
                loading.setVisibility(View.INVISIBLE);
                setSnackBar(parentLayout, "Login Failed");
            }
        });
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
                            loading.setVisibility(View.INVISIBLE);
                            ((LoginActivity)getActivity()).setSignUpCredentials(Email, Password);
                            ((LoginActivity)getActivity()).openVerifyEmail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                setSnackBar(parentLayout, "Login Failed");
            }
        });

    }

    private void checkProfileCreated() {
        String UserUID=mAuth.getCurrentUser().getUid();
        firestore.collection("NewUsers").document(UserUID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        loading.setVisibility(View.INVISIBLE);
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

}