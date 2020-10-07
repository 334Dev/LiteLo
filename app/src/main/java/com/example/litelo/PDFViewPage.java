package com.example.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewPage extends AppCompatActivity {
    PDFView pdfView;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view_page);
        pdfView=findViewById(R.id.pdfView);
        file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Lite.pdf");
        pdfView.fromFile(file).load();


        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

    }
}