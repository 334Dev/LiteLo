package com.dev334.litelo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dev334.litelo.Login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

            private CircleImageView profileImageView;
            private TextView profile_name,profile_Regno,profile_Grp;
            private Button logoutBtn;

            private FirebaseAuth mAuth;

           String UserID;
           StorageReference storageReference;


            private FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //instantiating firebase auth and firestore
        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        profileImageView=findViewById(R.id.profile_picMain);
        profile_name=findViewById(R.id.profile_name);
        profile_Regno=findViewById(R.id.profile_regNo);
        profile_Grp=findViewById(R.id.profile_Grp);
        logoutBtn=findViewById(R.id.logout_profile);

        //getting current userID
        UserID=mAuth.getCurrentUser().getUid();
        storageReference=FirebaseStorage.getInstance().getReference();

        //getting image from firestore
        StorageReference profileRef=storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   Picasso.get().load(uri).into(profileImageView);
               }
        });

        //choosing image from gallery
        profileImageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(ProfileActivity.this);
               }
        });

        //setting profile details
        setProfileDetails();

        //logout button functionality
        logoutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mAuth.signOut();
                 Intent i= new Intent(ProfileActivity.this, LoginActivity.class);
                 startActivity(i);
                 finish();
             }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            //get cropped image and upload to firebase
            if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                    Uri resultUri=result.getUri();
                    profileImageView.setImageURI(resultUri);
                    uploadImageToFirebase(resultUri);
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
                 Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
             }
         });

    }

    //setting all the fields of profile
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