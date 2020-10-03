package com.example.litelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {

            private CircleImageView profileImageView;
            private TextView profile_name,profile_Regno,profile_Grp,logoutBtn;

            private FirebaseAuth mAuth;

           String UserID;
           StorageReference storageReference;


            private FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

           mAuth=FirebaseAuth.getInstance();

           profileImageView=findViewById(R.id.profile_picMain);
           profile_name=findViewById(R.id.profile_name);
           profile_Regno=findViewById(R.id.profile_regNo);
           profile_Grp=findViewById(R.id.profile_Grp);
           logoutBtn=findViewById(R.id.logout_profile);
                   fstore=FirebaseFirestore.getInstance();
        UserID=mAuth.getCurrentUser().getUid();
           storageReference=FirebaseStorage.getInstance().getReference();
           StorageReference profileRef=storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");

           profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   Picasso.get().load(uri).into(profileImageView);

               }
           });


           profileImageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                       // open Gallery
                    Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalleryIntent,1000);

               }
           });

         setProfileDetails();

         logoutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mAuth.signOut();
                 Intent i= new Intent(profileActivity.this,MainActivity.class);
                 startActivity(i);
                 finish();
             }
         });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri ImageUri=data.getData();
                profileImageView.setImageURI(ImageUri);

                uploadImageToFirebase(ImageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri ImageUri){
        //uploading image to firebase storage
         final StorageReference fileRef=storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
         fileRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         Picasso.get().load(uri).into(profileImageView);
                     }
                 });
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(profileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
             }
         });

    }

    private void setProfileDetails(){
        fstore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("Name");
                String group=documentSnapshot.getString("Group");
                String reg=documentSnapshot.getString("RegNo");

                  profile_name.setText(name);
                  profile_Grp.setText(group);
                  profile_Regno.setText(reg);

            }
        });
    }
}