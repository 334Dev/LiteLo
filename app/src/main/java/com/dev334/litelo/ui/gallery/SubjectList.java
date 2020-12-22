package com.dev334.litelo.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dev334.litelo.MainActivity;
import com.dev334.litelo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import static com.dev334.litelo.HomeActivity.soundState;

public class SubjectList extends Fragment {

    private subjectListModel subjectListModel;
    private Button del,changePasswordBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private EditText passwordEditText;
    private EditText RePasswordEditText;
    private Context context;

    public static SwitchCompat soundEffects;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";  //sound

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subjectListModel =
                ViewModelProviders.of(this).get(subjectListModel.class);
        final View root = inflater.inflate(R.layout.fragment_subjectlist, container, false);
        del=root.findViewById(R.id.buttonDeleteAccount);
        passwordEditText=root.findViewById(R.id.editTextPassword);
        soundEffects=root.findViewById(R.id.sounds_switch);
        RePasswordEditText=root.findViewById(R.id.editTextRePassword);
        changePasswordBtn=root.findViewById(R.id.setNewPassword);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        context=getContext();

        loadData();
        soundEffects.setChecked(soundState);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=passwordEditText.getText().toString();
                String RePass=RePasswordEditText.getText().toString();
                if(pass.equals(RePass))
                {
                    if(pass.equals("")){
                        Toast.makeText(getContext(),"Empty field",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        firebaseUser.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Try Login Again ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Password didn't match ",Toast.LENGTH_LONG).show();
                }

            }
        });


        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dailog=new AlertDialog.Builder(getContext());
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
                                    Toast.makeText(getContext(),"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
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



        return root;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, soundEffects.isChecked());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        soundState = sharedPreferences.getBoolean(SWITCH1, true);
    }

    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
    }
}