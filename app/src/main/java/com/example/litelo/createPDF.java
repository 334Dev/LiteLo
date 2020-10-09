package com.example.litelo;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class createPDF extends AppCompatActivity {
    Button generate;
    private Float y=17.368f;
    private String[] KeyDataR={"Mobile","Email","GitHub","LinkedIn"};
    private List<String> ValueDataR;
    private String[] KeyDataL={"Discipline","Branch","College"};
    private List<String> ValueDataL;
    private String[] eduProgress={"B.Tech, Electronics and Communication","X CBSE","XII CBSE"};
    private String[] eduInstitute={"MNNIT Allahabad","Army Public School, N.R.","Army Public School, N.R."};
    private String[] eduCGPA={"7.7","10","94.4"};
    private String[] eduYear={"2023","2017","2019"};
    private  String[] areasOfInterest={"Data Structures and Algorithms","Android Development","Competitive Programming","Software Development"};
    private  String[] computerSkillKey={"Programming Languages","Android","Video Editing"};
    private  String[] computerSkillValue={"C,C++,Java,Python","Native Development","Adobe Premiere Pro"};
    private String[] ProjectName={"Yantriki","Golden Ratio App","LiteLo"};
    private String[] project0={"An android quiz app for conducting timer based quiz","Technology Stack: Android Studio, Firebase, Adobe Illustrator, Adobe XD.","https://github.com/SAEapp/Automobile-Quiz-App-2"};
    private String[] project1={"An android app for educating people about golden ratio.","Technology Stack: Android Studio, Firebase, Google ML Kit, Adobe After effects, Adobe Illustrator Adobe XD.","https://github.com/iamanantshukla/GoldenRatio"};
    private String[] project2={"An android quiz app for conducting timer based quiz","Technology Stack: Android Studio, Firebase, Adobe Illustrator, Adobe XD.","https://github.com/SAEapp/Automobile-Quiz-App-2"};
    private String[] Hobbies={"Listening Music", "Badminton","Cinematography"};
    private String[] Awards={"Secured 1st Position in “Cisco Thinqbator design challenge Virtual Hackathon” for working under\n" +
            "the prototype of Covid Tracking and Temperature Monitoring Model","Secured 2nd Position in “The Mighty Pen-Eloquence 2020” Annual Literary Fest of MNNIT Allahabad."
            ,"Secured 2nd Position in Debate-Eloquence 2020” Annual Literary Fest of MNNIT Allahabad."};


    private Float xR=120.0f;
    private Float MainY=54.86f;
    private Float yR=y+3.0f;
    private Float yL=y+10.0f;

    private Integer PERSONAL_FLAG=0;

    private LinearLayout personalGroup;
    private LinearLayout personalItems;

    //personalInfo
    private TextView Name, Email, Github, LinkedIn,Mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_p_d_f);
        generate=findViewById(R.id.genButton);
        personalGroup=findViewById(R.id.PersonalGroup);
        personalItems=findViewById(R.id.personalItems);

        //personal info
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        Mobile=findViewById(R.id.mobile);
        Github=findViewById(R.id.github);
        LinkedIn=findViewById(R.id.linkedIn);


        personalGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PERSONAL_FLAG==0) {
                    personalItems.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    PERSONAL_FLAG=1;
                }else{
                    personalItems.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    PERSONAL_FLAG=0;
                }
            }
        });
        CreatePdf();
    }




    private void CreatePdf() {
        final int i;
        generate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                getTextField();
                PdfDocument MyPdfDocument = new PdfDocument();
                TextPaint myPaint = new TextPaint();
                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.
                        Builder(210, 297, 1).create();
                PdfDocument.Page myPage1 = MyPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();

                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                myPaint.setTextSize(9.131f);

                canvas.drawText(Name.getText().toString(), 8, y, myPaint);

                myPaint.setTextSize(3.5f);
                myPaint.setLinearText(true);
                myPaint.setLetterSpacing(0.04f);


                myPaint.setStrokeJoin(TextPaint.Join.ROUND);
                myPaint.setStrokeCap(TextPaint.Cap.ROUND);

                for (int i = 0; i < 4; i++) {
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText(KeyDataR[i] + " : ", 116, yR, myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(ValueDataR.get(i), 134, yR, myPaint);
                    yR = yR + 7.0f;

                }

                for (int i = 0; i < 3; i++)
                {
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText(KeyDataL[i] + " : ", 8, yL, myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(ValueDataL.get(i), 30, yL, myPaint);
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

                StaticLayout smallStaticLayout=StaticLayout.Builder.obtain(str,0,str.length()-1,myPaint,194).build();

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

                canvas.translate(0,-27);
                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,MainY+ObjectiveHeight +10f,202f,MainY+ObjectiveHeight +10f,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Areas of Interests", 10,MainY+ObjectiveHeight +11.2f,myPaint);

                canvas.translate(-107,40);
                myPaint.setTextSize(3.5f);
                for (int i=0;i<4;i++)
                {
                    MainY=MainY+5f;
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText("•", 116, MainY,myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(areasOfInterest[i], 120, MainY, myPaint);

                }

                canvas.translate(107,-27);
                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,MainY+ObjectiveHeight +10f,202f,MainY+ObjectiveHeight +10f,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);

                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Skills", 10,MainY+ObjectiveHeight +11.2f,myPaint);

                canvas.translate(-107,40);
                myPaint.setTextSize(3.5f);
                for (int i=0;i<3;i++)
                {
                    MainY=MainY+5f;
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText("•  "+computerSkillKey[i], 116, MainY,myPaint);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText(": "+computerSkillValue[i], 180, MainY, myPaint);

                }



                canvas.translate(107,-27);
                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,MainY+ObjectiveHeight +10f,202f,MainY+ObjectiveHeight+10f,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);

                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Projects", 10,MainY+ObjectiveHeight+11.2f,myPaint);
                MainY=MainY+ObjectiveHeight+11.2f-5f;

                Log.i("ProjectName_Length", "onClick: "+ProjectName.length);
                myPaint.setTextSize(3.5f);
                for(int i=0;i<ProjectName.length;i++){
                    MainY=MainY+12f;
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                    canvas.drawText(ProjectName[i],8,MainY,myPaint);
                    for(int j=0;j<3;j++){
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium), Typeface.NORMAL));
                        if(i==0) {
                            str = "•  " + project0[j];
                        }else if(i==1){
                            str = "•  " + project1[j];
                        }else if(i==2){
                            str = "•  " + project2[j];
                        }
                        StaticLayout Proj=StaticLayout.Builder.obtain(str,0,str.length()-1,myPaint,190).build();
                        canvas.translate(12,MainY);
                        MainY=MainY+Proj.getHeight()+5f;
                        Proj.draw(canvas);
                        canvas.translate(-12,-MainY);
                        MainY=MainY+5f;
                    }
                    MainY=MainY-5f;
                }

                myPaint.setColor(Color.parseColor("#D3D3D3"));
                myPaint.setStrokeWidth(7f);
                canvas.drawLine(8f,MainY+12f,202f,MainY+12f,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);

                myPaint.setTextSize(5f);
                myPaint.setColor(Color.parseColor("#000000"));
                myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                canvas.drawText("Hobbies", 10,MainY+13.2f,myPaint);
                MainY=MainY+13.2f;
                myPaint.setTextSize(3.5f);
                MainY=MainY+8f;
                for(int i=0;i<Hobbies.length;i++){
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                    canvas.drawText("•  "+Hobbies[i], 8, MainY, myPaint);
                    MainY=MainY+5f;
                }
                MainY=MainY-5f;
                Integer neededSpace=Awards.length*8+30;
                if(297-MainY>neededSpace){
                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f,MainY+12f,202f,MainY+12f,myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Hobbies", 10,MainY+13.2f,myPaint);
                    MainY=MainY+13.2f;
                    myPaint.setTextSize(3.5f);
                    MainY=MainY+4f;
                    for(int i=0;i<Awards.length;i++){
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        str="•  "+Awards[i];
                        StaticLayout awards=StaticLayout.Builder.obtain(str,0,str.length()-1,myPaint,192).build();
                        ObjectiveHeight=awards.getHeight();
                        canvas.translate(8,MainY);
                        awards.draw(canvas);
                        canvas.translate(-8,-MainY);
                        MainY=MainY+ObjectiveHeight+2f;
                    }
                    MyPdfDocument.finishPage(myPage1);
                }else{
                    MyPdfDocument.finishPage(myPage1);
                    PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.
                            Builder(210, 297, 2).create();
                    PdfDocument.Page myPage2 = MyPdfDocument.startPage(myPageInfo2);

                    Canvas canvas2 = myPage2.getCanvas();

                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas2.drawLine(8f,12,202f,12,myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas2.drawText("Awards & Achievements", 10,13,myPaint);
                    MainY=13f;
                    myPaint.setTextSize(3.5f);
                    MainY=MainY+4f;
                    for(int i=0;i<Awards.length;i++){
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        str="•  "+Awards[i];
                        StaticLayout awards=StaticLayout.Builder.obtain(str,0,str.length()-1,myPaint,192).build();
                        ObjectiveHeight=awards.getHeight();
                        canvas2.translate(8,MainY);
                        awards.draw(canvas2);
                        canvas2.translate(-8,-MainY);
                        MainY=MainY+ObjectiveHeight+2f;
                    }
                    MyPdfDocument.finishPage(myPage2);
                }

                y=17.368f;
                MainY=54.86f;
                yR=y+3.0f;
                yL=y+10.0f;


                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Lite.pdf");
                try {
                    MyPdfDocument.writeTo(new FileOutputStream(file));
                    Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),PDFViewPage.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                }
                MyPdfDocument.close();
            }
        });
    }

    private void getTextField() {
        ValueDataR=new ArrayList<>();
        ValueDataR.add(Mobile.getText().toString());
        ValueDataR.add(Email.getText().toString());
        ValueDataR.add(Github.getText().toString());
        ValueDataR.add(LinkedIn.getText().toString());

    }
}