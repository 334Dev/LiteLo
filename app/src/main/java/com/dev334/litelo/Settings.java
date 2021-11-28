package com.dev334.litelo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev334.litelo.Login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    private Button del,changePasswordBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private EditText passwordEditText;
    private EditText RePasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        del=findViewById(R.id.buttonDeleteAccount);
        passwordEditText=findViewById(R.id.editTextPassword);
        RePasswordEditText=findViewById(R.id.editTextRePassword);
        changePasswordBtn=findViewById(R.id.setNewPassword);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=passwordEditText.getText().toString();
                String RePass=RePasswordEditText.getText().toString();
                if(pass.equals(RePass))
                {
                    if(pass.equals("")){
                        Toast.makeText(getApplicationContext(),"Empty field",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        firebaseUser.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Try Login Again ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password didn't match ",Toast.LENGTH_LONG).show();
                }

            }
        });


        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dailog=new AlertDialog.Builder(Settings.this);
                dailog.setTitle("Are you Sure ?");
                dailog.setMessage("Deleting your account will delete all your Info from LiteLo");
                dailog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(Settings.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dailog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dailog.create();
                alertDialog.show();
            }
        });
    }
}