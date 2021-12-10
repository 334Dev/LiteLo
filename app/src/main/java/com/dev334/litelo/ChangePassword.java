package com.dev334.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private Button changePasswordBtn;
    private EditText passwordEditText, RePasswordEditText;
    private FirebaseUser firebaseUser;
    private static String TAG="ChangePasswordAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changePasswordBtn=findViewById(R.id.resetPassBtn);
        passwordEditText=findViewById(R.id.resetPasswordNew);
        RePasswordEditText=findViewById(R.id.resetPasswordConfirm);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Pass= String.valueOf(passwordEditText.getText());
                String cPass= String.valueOf(RePasswordEditText.getText());

                if(Pass.isEmpty()){
                    passwordEditText.setError("Field is empty");
                }else{
                    String Email=firebaseUser.getEmail();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, Pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    firebaseUser.updatePassword(cPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i(TAG, "onFailure: "+e.getMessage());
                                            Toast.makeText(getApplicationContext(),"Try Login Again ",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e.getMessage());
                            passwordEditText.setError("Recheck password");
                        }
                    });
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}