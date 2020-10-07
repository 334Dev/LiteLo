package com.example.litelo.ui.slideshow;

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

import com.example.litelo.R;
import com.example.litelo.createPDF;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ImageView createImg;
    private TextView createTxt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createImg=root.findViewById(R.id.createImg);
        createTxt=root.findViewById(R.id.createTxt);
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

        return root;
    }
    void createpdf(){
        Intent i= new Intent(getActivity(), createPDF.class);
        startActivity(i);
    }


}