package com.dev334.litelo.ui.Pdfcreate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dev334.litelo.PDFViewPage;
import com.dev334.litelo.R;
import com.dev334.litelo.createPDF;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class createPdfFragment extends Fragment {

    private createPdfViewModel slideshowViewModel;
    private ImageView createImg,ViewPdfImg;
    private TextView createTxt,greetingTxt,viewPdfText;
    private FirebaseAuth mAuth;
    private String UserID;
    private FirebaseFirestore firestore;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel =
                ViewModelProviders.of(this).get(createPdfViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createImg=root.findViewById(R.id.createImg);
        createTxt=root.findViewById(R.id.createTxt);
        viewPdfText=root.findViewById(R.id.textView13);
        ViewPdfImg=root.findViewById(R.id.imageView7);
        createTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createpdf();
            }
        });
        createImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createpdf();
            }
        });
        viewPdfText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf();
            }
        });
        ViewPdfImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPdf();
            }
        });
        mAuth=FirebaseAuth.getInstance();
        UserID=mAuth.getCurrentUser().getUid();
        firestore=FirebaseFirestore.getInstance();
        greetingTxt=root.findViewById(R.id.greeting_resume);



        firestore.collection("Users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("Name");

                String lastName = "";
                String firstName= "";
                if(name.split("\\w+").length>1){

                    lastName = name.substring(name.lastIndexOf(" ")+1);
                    firstName = name.substring(0, name.lastIndexOf(' '));
                }
                else{
                    firstName = name;
                }


                greetingTxt.setText("Hi "+firstName+",");


            }
        });






        return root;
    }
    void createpdf(){
        Intent i= new Intent(getActivity(), createPDF.class);
        startActivity(i);
    }
    void viewPdf(){
        Intent i= new Intent(getActivity(), PDFViewPage.class);
        startActivity(i);
    }


}