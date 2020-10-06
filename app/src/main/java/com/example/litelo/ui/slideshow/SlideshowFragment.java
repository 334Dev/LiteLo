package com.example.litelo.ui.slideshow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.litelo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    Button generate;
    private Float y=17.368f;
    private String[] KeyDataR={"Mobile","Email","GitHub","LinkedIn"};
    private String[] ValueDataR={"+914234677431","mittal11darpan@gmail.com","github.com/iamanantshukla","linkedin.com/in/anant-shukla-16b1231b3/"};
    private String[] KeyDataL={"Discipline","Branch","College"};
    private String[] ValueDataL={"Bachelor of Technology","Electronics and Communication Engineering","Motilal Nehru National Institute of Technology"};
    private String[] eduProgress={"B.Tech, Electronics and Communication","X CBSE","XII CBSE"};
    private String[] eduInstitute={"MNNIT Allahabad","Army Public School, N.R.","Army Public School, N.R."};
    private String[] eduCGPA={"7.7","10","94.4"};
    private String[] eduYear={"2023","2017","2019"};
    private Float xR=120.0f;
    private Float MainY=54.86f;
    private Float yR=y+3.0f;
    private Float yL=y+10.0f;

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
        final int i;
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument MyPdfDocument = new PdfDocument();
                TextPaint myPaint = new TextPaint();
                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.
                        Builder(210, 297, 1).create();
                PdfDocument.Page myPage1 = MyPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();

                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                myPaint.setTextSize(9.131f);

                canvas.drawText("Anant Shukla", 8, y, myPaint);

                myPaint.setTextSize(3.5f);
                myPaint.setLinearText(true);
                myPaint.setLetterSpacing(0.04f);


                for (int i = 0; i < 4; i++) {
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText(KeyDataR[i] + " : ", 116, yR, myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(ValueDataR[i], 134, yR, myPaint);
                    yR = yR + 7.0f;

                }

                for (int i = 0; i < 3; i++)
                {
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText(KeyDataL[i] + " : ", 8, yL, myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(ValueDataL[i], 30, yL, myPaint);
                    yL=yL+7.0f;
                }
                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,54.86f,202f,54.86f,myPaint);

                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Career Objective", 10,56.5f,myPaint);
                myPaint.setTextSize(3.5f);
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium),Typeface.NORMAL));
                String str="To use my problem solving skills to solve real life problems and help the world to become better and more connected."+
                        "To use my problem solving skills to solve real life problems and help the world to become better and more connected."+
                        "To use my problem solving skills to solve real life problems and help the world to become better and more connected."+
                        "To use my problem solving skills to solve real life problems and help the world to become better and more connected."+
                        "To use my problem solving skills to solve real life problems and help the world to become better and more connected.";

                StaticLayout smallStaticLayout=StaticLayout.Builder.obtain(str,0,str.length()-1,myPaint,184).build();

                canvas.translate(8,60);
                smallStaticLayout.draw(canvas);


                int ObjectiveHeight=smallStaticLayout.getHeight();
                canvas.translate(-8,-60);
                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,MainY+ObjectiveHeight +10f,202f,MainY+ObjectiveHeight +10f,myPaint);

                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Education", 10,MainY+ObjectiveHeight +11.2f,myPaint);

                MainY=MainY+ObjectiveHeight+11.2f;

                myPaint.setTextSize(3.5f);
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));

                canvas.drawText("Progress",8,MainY+8f,myPaint);
                canvas.drawText("Institute",100,MainY+8f,myPaint);
                canvas.drawText("%CGPA",143,MainY+8f,myPaint);
                canvas.drawText("Year of Completion",167,MainY+8f,myPaint);
                MainY=MainY+8f;
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium), Typeface.NORMAL));
                for(int i=0;i<3;i++){
                    MainY=MainY+8f;
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(eduProgress[i],8,MainY,myPaint);
                    myPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(eduInstitute[i],110,MainY,myPaint);
                    canvas.drawText(eduCGPA[i],151,MainY,myPaint);
                    canvas.drawText(eduYear[i],184,MainY,myPaint);
                }


                MyPdfDocument.finishPage(myPage1);




                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Lite.pdf");
                try {
                    MyPdfDocument.writeTo(new FileOutputStream(file));
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                }
                MyPdfDocument.close();
            }
        });
    }
}