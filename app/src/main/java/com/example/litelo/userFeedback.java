package com.example.litelo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userFeedback extends AppCompatActivity {

    private TextView Name, Subject, Desc;
    private Button send;
    private ProgressBar loading;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);

        loading= findViewById(R.id.feedbackLoad);
        Name= findViewById(R.id.nameText);
        Subject=findViewById(R.id.subjectText);
        Desc=findViewById(R.id.descText);

        Name.setHintTextColor(getResources().getColor(R.color.colorAccent));
        Subject.setHintTextColor(getResources().getColor(R.color.colorAccent));
        Desc.setHintTextColor(getResources().getColor(R.color.colorAccent));

        send=findViewById(R.id.sendBtn);
        fstore=FirebaseFirestore.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty()){
                    Name.setError("Name is empty");
                }else if(Subject.getText().toString().isEmpty()){
                    Subject.setError("Subject is empty");
                }else if(Desc.getText().toString().isEmpty()){
                    Desc.setError("Description is empty");
                }else{
                    Map<String, Object> map=new HashMap<>();
                    map.put("Name",Name.getText().toString());
                    map.put("Subject",Subject.getText().toString());
                    map.put("Desc",Desc.getText().toString());

                    fstore.collection("Feedback").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("SendFeedback", "onSuccess: Feedback Sent");
                            Toast.makeText(getApplicationContext(),"Feedback Sent",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("SendFeedback", "onFailure: "+e.getMessage());
                        }
                    });
                }
            }
        });


    }
}