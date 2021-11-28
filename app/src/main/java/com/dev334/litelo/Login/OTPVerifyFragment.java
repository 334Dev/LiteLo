package com.dev334.litelo.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import static com.dev334.litelo.Login.signUpFragment.setSnackBar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev334.litelo.Database.TinyDB;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OTPVerifyFragment extends Fragment {

    private View view;
    private TextView editOTP;
    private Button verify;
    private TinyDB tinyDB;
    private String PhoneNo, VerificationID;
    private ConstraintLayout parentLayout;
    private ProgressBar loading;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG="OTPVerifyFragment";
    private FirebaseFirestore firestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_o_t_p_verify, container, false);

        editOTP=view.findViewById(R.id.EditOTPTxt);
        verify=view.findViewById(R.id.VerifyOTP);
        tinyDB=new TinyDB(getContext());
        parentLayout=view.findViewById(R.id.OTPFragmentLayout);
        loading=view.findViewById(R.id.loadingOTP);
        firestore=FirebaseFirestore.getInstance();

        loading.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();

        PhoneNo=((LoginActivity)getActivity()).getPhoneNo();
        VerificationID=((LoginActivity)getActivity()).getVerificationID();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP;
                OTP=editOTP.getText().toString();

                if(OTP.isEmpty()){
                    editOTP.setError("Enter valid OTP");
                }else{
                    loading.setVisibility(View.VISIBLE);
                    verifyPhoneNumberWithCode(VerificationID, OTP);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //not needed
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
            }
        };

        return view;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkProfileCreated();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "signInWithCredential:failure "+e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    setSnackBar(parentLayout, "Invalid OTP");
                }
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
                            Log.i(TAG, "onComplete: "+task.toString());
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            startActivity(i);
                        }else{
                            ((LoginActivity)getActivity()).openCreateProfile();
                        }
                    }
                });
    }

}