package com.example.litelo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class PDFViewPage extends AppCompatActivity {
    PDFView pdfView;
    File file;
    private FloatingActionButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view_page);
        pdfView=findViewById(R.id.pdfView);
        file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Lite.pdf");

        pdfView.fromFile(file).spacing(6).load();
        pdfView.setBackgroundColor(Color.BLACK);
        shareButton=findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri pdfUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Lite.pdf");
                sharingIntent.setType("*/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });


        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

    }
}