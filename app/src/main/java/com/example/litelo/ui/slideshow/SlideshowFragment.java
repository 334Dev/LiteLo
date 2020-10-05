package com.example.litelo.ui.slideshow;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.litelo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    Button generate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        generate=root.findViewById(R.id.genButton);

        ActivityCompat.requestPermissions(getActivity(),new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        CreatePdf();




        return root;
    }

    private void CreatePdf() {
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument MyPdfDocument=new PdfDocument();
                Paint myPaint=new Paint();
                PdfDocument.PageInfo myPageInfo1=new PdfDocument.PageInfo.
                        Builder(250,400,1).create();
                PdfDocument.Page myPage1=MyPdfDocument.startPage(myPageInfo1);
                Canvas canvas=myPage1.getCanvas();
                canvas.drawText("Hello everyone !!",40 ,50,myPaint);
                MyPdfDocument.finishPage(myPage1);
                File file=new File(Environment.getExternalStorageDirectory(),"/LiteLOOOOO.pdf");
                try {
                    MyPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyPdfDocument.close();
            }
        });
    }
}