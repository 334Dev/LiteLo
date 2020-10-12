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
    private static final String TAG = "EmptyField" ;
    Button generate;
    private Float y=17.368f;
    private String[] KeyDataR={"Mobile","Email","GitHub","LinkedIn"};
    private List<String> ValueDataR;
    private String[] KeyDataL={"Discipline","Branch","College"};
    private List<String> ValueDataL;
    private String[] eduProgress={"B.Tech","X CBSE","XII CBSE"};
    private List<String> eduInstitute; //={"MNNIT Allahabad","Army Public School, N.R.","Army Public School, N.R."};
    private List<String> eduCGPA; //={"7.7","10","94.4"};
    private List<String> eduYear; //={"2023","2017","2019"};
    private List<String> areasOfInterest; //={"Data Structures and Algorithms","Android Development","Competitive Programming","Software Development"};
    private List<String> computerSkillKey; //={"Programming Languages","Android","Video Editing"};
    private List<String> computerSkillValue; //={"C,C++,Java,Python","Native Development","Adobe Premiere Pro"};
    private List<String> ProjectName; //={"Yantriki","Golden Ratio App","LiteLo"};
    private List<String> project0; //={"An android quiz app for conducting timer based quiz","Technology Stack: Android Studio, Firebase, Adobe Illustrator, Adobe XD.","https://github.com/SAEapp/Automobile-Quiz-App-2"};
    private List<String> project1; //={"An android app for educating people about golden ratio.","Technology Stack: Android Studio, Firebase, Google ML Kit, Adobe After effects, Adobe Illustrator Adobe XD.","https://github.com/iamanantshukla/GoldenRatio"};
    private List<String> project2; //={"An android quiz app for conducting timer based quiz","Technology Stack: Android Studio, Firebase, Adobe Illustrator, Adobe XD.","https://github.com/SAEapp/Automobile-Quiz-App-2"};
    private List<String> Hobbies; //={"Listening Music", "Badminton","Cinematography"};
    private List<String> Awards; //={"Secured 1st Position in “Cisco Thinqbator design challenge Virtual Hackathon” for working under\n" +
            //"the prototype of Covid Tracking and Temperature Monitoring Model","Secured 2nd Position in “The Mighty Pen-Eloquence 2020” Annual Literary Fest of MNNIT Allahabad."
            //,"Secured 2nd Position in Debate-Eloquence 2020” Annual Literary Fest of MNNIT Allahabad."};


    private Float xR=120.0f;
    private Float MainY=54.86f;
    private Float yR=y+3.0f;
    private Float yL=y+10.0f;

    private Integer PERSONAL_FLAG=0, CURRENT_INSTITUTE_FLAG=0,CAREER_FLAG=0,EDUCATION_FLAG=0,INTEREST_FLAG=0,SKILLS_FLAG=0,
            PROJECT_FLAG=0, AWARDS_FLAG=0, HOBBY_FLAG=0;

    private LinearLayout personalGroup,currentInstitute,careerObj,education,interest,skills,project,awards,hobby;
    private LinearLayout personalItems,currentInstituteList,careerList,educationList,interestList,skillsList,projectList,awardsList,hobbyList;

    //personalInfo
    private TextView Name, Email, Github, LinkedIn,Mobile;

    //current Institute info
    private TextView branch, discipline, institute;

    //education
    private TextView xSchool, xiiSchool, collegeName, xYear, xiiYear, cYear, xCgpa, xiiCgpa, cCgpa;

    //area of interest
    private TextView interest1,interest2, interest3, interest4, interest5;

    //careerObjective
    private TextView careerObjective;

    //skills info
    private TextView skillType1, skillType2, skillType3;
    private TextView skillList1,skillList2,skillList3;

    //projects info
    private TextView projectName1,projectName2,projectName3;
    private TextView projectPoint11,projectPoint21,projectPoint31;
    private TextView projectPoint12,projectPoint22,projectPoint32;
    private TextView projectPoint13,projectPoint23,projectPoint33;

    //hobby
    private TextView hobby1,hobby2,hobby3,hobby4,hobby5;

    //awards and achievements
    private TextView award1,award2,award3,award4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_p_d_f);
        generate=findViewById(R.id.genButton);

        personalGroup=findViewById(R.id.PersonalGroup);
        personalItems=findViewById(R.id.personalItems);

        currentInstitute=findViewById(R.id.currentInstitute);
        currentInstituteList=findViewById(R.id.currentInstituteList);

        careerObj=findViewById(R.id.careerObj);
        careerList=findViewById(R.id.ObjectiveList);

        education=findViewById(R.id.education);
        educationList=findViewById(R.id.educationList);

        interest=findViewById(R.id.InterestGroup);
        interestList=findViewById(R.id.InterestItems);

        skills=findViewById(R.id.skills);
        skillsList=findViewById(R.id.skillsList);

        project=findViewById(R.id.project);
        projectList=findViewById(R.id.projectList);

        awards=findViewById(R.id.awards);
        awardsList=findViewById(R.id.awardList);

        hobby=findViewById(R.id.HobbyGroup);
        hobbyList=findViewById(R.id.HobbyItems);

        //personal info
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        Mobile=findViewById(R.id.mobile);
        Github=findViewById(R.id.github);
        LinkedIn=findViewById(R.id.linkedIn);

        //current Institute detail
        branch=findViewById(R.id.branch);
        discipline=findViewById(R.id.discipline);
        institute=findViewById(R.id.college);

        //education detail
        xSchool=findViewById(R.id.schoolX);
        xCgpa=findViewById(R.id.Xcgpa);
        xYear=findViewById(R.id.Xpass);

        xiiSchool=findViewById(R.id.schoolXII);
        xiiCgpa=findViewById(R.id.XIIcgpa);
        xiiYear=findViewById(R.id.XIIpass);

        collegeName=findViewById(R.id.collegeName);
        cCgpa=findViewById(R.id.Ccgpa);
        cYear=findViewById(R.id.Cpass);

        //Area of interest details
        interest1=findViewById(R.id.interest1);
        interest2=findViewById(R.id.interest2);
        interest3=findViewById(R.id.interest3);
        interest4=findViewById(R.id.interest4);
        interest5=findViewById(R.id.interest5);

        //career Objective
        careerObjective=findViewById(R.id.objective);

        //skills
        skillType1=findViewById(R.id.skillType1);
        skillType2=findViewById(R.id.skillType2);
        skillType3=findViewById(R.id.skillType3);

        skillList1=findViewById(R.id.skillList1);
        skillList2=findViewById(R.id.skillList2);
        skillList3=findViewById(R.id.skillList3);

        //projects
        projectName1=findViewById(R.id.pro1name3);
        projectName2=findViewById(R.id.pro2name2);
        projectName3=findViewById(R.id.pro3name);

        projectPoint11=findViewById(R.id.pro1Desc7);
        projectPoint12=findViewById(R.id.pro2Desc4);
        projectPoint13=findViewById(R.id.pro3Desc1);

        projectPoint21=findViewById(R.id.pro1Desc8);
        projectPoint22=findViewById(R.id.pro2Desc5);
        projectPoint23=findViewById(R.id.pro3Desc2);

        projectPoint31=findViewById(R.id.pro1Desc9);
        projectPoint32=findViewById(R.id.pro2Desc6);
        projectPoint33=findViewById(R.id.pro3Desc3);

        //hobby info
        hobby1=findViewById(R.id.hobby1);
        hobby2=findViewById(R.id.hobby2);
        hobby3=findViewById(R.id.hobby3);
        hobby4=findViewById(R.id.hobby4);
        hobby5=findViewById(R.id.hobby5);

        //awards
        award1=findViewById(R.id.award1);
        award2=findViewById(R.id.award2);
        award3=findViewById(R.id.award3);
        award4=findViewById(R.id.award4);


        personalGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PERSONAL_FLAG==0) {
                    collapseAll();
                    personalItems.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    PERSONAL_FLAG=1;
                }else{
                    personalItems.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    PERSONAL_FLAG=0;
                }
            }
        });

        currentInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CURRENT_INSTITUTE_FLAG==0) {
                    collapseAll();
                    currentInstituteList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    CURRENT_INSTITUTE_FLAG=1;
                }else{
                    currentInstituteList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    CURRENT_INSTITUTE_FLAG=0;
                }
            }
        });

        careerObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CAREER_FLAG==0) {
                    collapseAll();
                    careerList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    CAREER_FLAG=1;
                }else{
                    careerList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    CAREER_FLAG=0;
                }
            }
        });

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EDUCATION_FLAG==0) {
                    collapseAll();
                    educationList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    EDUCATION_FLAG=1;
                }else{
                    educationList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    EDUCATION_FLAG=0;
                }
            }
        });

        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INTEREST_FLAG==0) {
                    collapseAll();
                    interestList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    INTEREST_FLAG=1;
                }else{
                    interestList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    INTEREST_FLAG=0;
                }
            }
        });

        skills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SKILLS_FLAG==0) {
                    collapseAll();
                    skillsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    SKILLS_FLAG=1;
                }else{
                    skillsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    SKILLS_FLAG=0;
                }
            }
        });

        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PROJECT_FLAG==0) {
                    collapseAll();
                    projectList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    PROJECT_FLAG=1;
                }else{
                    projectList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    PROJECT_FLAG=0;
                }
            }
        });

        awards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AWARDS_FLAG==0) {
                    collapseAll();
                    awardsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    AWARDS_FLAG=1;
                }else{
                    awardsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    AWARDS_FLAG=0;
                }
            }
        });

        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HOBBY_FLAG==0) {
                    collapseAll();
                    hobbyList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    HOBBY_FLAG=1;
                }else{
                    hobbyList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    HOBBY_FLAG=0;
                }
            }
        });

        CreatePdf();
    }

    private void collapseAll() {
        personalItems.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        currentInstituteList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        awardsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        hobbyList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        skillsList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        projectList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        careerList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        educationList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
        interestList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }


    private void CreatePdf() {
        final int i;
        generate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                if (checkField()) {
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

                    for (int i = 0; i < 3; i++) {
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                        canvas.drawText(KeyDataL[i] + " : ", 8, yL, myPaint);
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        canvas.drawText(ValueDataL.get(i), 30, yL, myPaint);
                        yL = yL + 7.0f;
                    }
                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, 54.86f, 202f, 54.86f, myPaint);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Career Objective", 10, 56.5f, myPaint);
                    myPaint.setTextSize(3.5f);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium), Typeface.NORMAL));
                    String str = careerObjective.getText().toString();

                    StaticLayout smallStaticLayout = StaticLayout.Builder.obtain(str, 0, str.length() - 1, myPaint, 194).build();

                    canvas.translate(8, 60);
                    smallStaticLayout.draw(canvas);


                    int ObjectiveHeight = smallStaticLayout.getHeight();
                    canvas.translate(-8, -60);
                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, MainY + ObjectiveHeight + 10f, 202f, MainY + ObjectiveHeight + 10f, myPaint);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Education", 10, MainY + ObjectiveHeight + 11.2f, myPaint);

                    MainY = MainY + ObjectiveHeight + 11.2f;

                    myPaint.setTextSize(3.5f);
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));

                    canvas.drawText("Progress", 8, MainY + 8f, myPaint);
                    canvas.drawText("Institute", 100, MainY + 8f, myPaint);
                    canvas.drawText("%CGPA", 143, MainY + 8f, myPaint);
                    canvas.drawText("Year of Completion", 167, MainY + 8f, myPaint);
                    MainY = MainY + 8f;
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium), Typeface.NORMAL));
                    for (int i = 0; i < 3; i++) {
                        MainY = MainY + 8f;
                        myPaint.setTextAlign(Paint.Align.LEFT);
                        canvas.drawText(eduProgress[i], 8, MainY, myPaint);
                        myPaint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(eduInstitute.get(i), 110, MainY, myPaint);
                        canvas.drawText(eduCGPA.get(i), 151, MainY, myPaint);
                        canvas.drawText(eduYear.get(i), 184, MainY, myPaint);
                    }

                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, MainY + 10f, 202f, MainY + 10f, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Areas of Interests", 10, MainY +  11.2f, myPaint);
                    MainY=MainY+11.2f+5f;
                    myPaint.setTextSize(3.5f);
                    for (int i = 0; i < areasOfInterest.size(); i++) {
                        MainY = MainY + 5f;
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                        canvas.drawText("•", 8, MainY, myPaint);
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        canvas.drawText(areasOfInterest.get(i), 10, MainY, myPaint);

                    }

                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, MainY +  10f, 202f, MainY +  10f, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Skills", 10, MainY +  11.2f, myPaint);

                    MainY=MainY + 11.2f+5f;
                    myPaint.setTextSize(3.5f);
                    for (int i = 0; i < computerSkillKey.size(); i++) {
                        MainY = MainY + 5f;
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                        canvas.drawText("•  " + computerSkillKey.get(i), 8, MainY, myPaint);
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        canvas.drawText(": " + computerSkillValue.get(i), 120, MainY, myPaint);

                    }


                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, MainY + 10f, 202f, MainY  + 10f, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Projects", 10, MainY +  11.2f, myPaint);
                    MainY = MainY + 11.2f -5f;

                    Log.i("ProjectName_Length", "onClick: " + ProjectName.size());
                    myPaint.setTextSize(3.5f);
                    for (int i = 0; i < ProjectName.size(); i++) {
                        MainY = MainY + 12f;
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.BOLD));
                        canvas.drawText(ProjectName.get(i), 8, MainY, myPaint);
                        for (int j = 0; j < 3; j++) {
                            myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_medium), Typeface.NORMAL));
                            if (i == 0) {
                                str = "•  " + project0.get(i);
                            } else if (i == 1) {
                                str = "•  " + project1.get(i);
                            } else if (i == 2) {
                                str = "•  " + project2.get(i);
                            }
                            StaticLayout Proj = StaticLayout.Builder.obtain(str, 0, str.length() - 1, myPaint, 190).build();
                            canvas.translate(12, MainY);
                            MainY = MainY + Proj.getHeight() + 5f;
                            Proj.draw(canvas);
                            canvas.translate(-12, -MainY);
                            MainY = MainY + 5f;
                        }
                        MainY = MainY - 5f;
                    }

                    myPaint.setColor(Color.parseColor("#D3D3D3"));
                    myPaint.setStrokeWidth(7f);
                    canvas.drawLine(8f, MainY + 12f, 202f, MainY + 12f, myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);

                    myPaint.setTextSize(5f);
                    myPaint.setColor(Color.parseColor("#000000"));
                    myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                    canvas.drawText("Hobbies", 10, MainY + 13.2f, myPaint);
                    MainY = MainY + 13.2f;
                    myPaint.setTextSize(3.5f);
                    MainY = MainY + 8f;
                    for (int i = 0; i < Hobbies.size(); i++) {
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                        canvas.drawText("•  " + Hobbies.get(i), 8, MainY, myPaint);
                        MainY = MainY + 5f;
                    }
                    MainY = MainY - 5f;
                    Integer neededSpace = Awards.size() * 8 + 30;
                    if (297 - MainY > neededSpace) {
                        myPaint.setColor(Color.parseColor("#D3D3D3"));
                        myPaint.setStrokeWidth(7f);
                        canvas.drawLine(8f, MainY + 12f, 202f, MainY + 12f, myPaint);

                        myPaint.setTextAlign(Paint.Align.LEFT);

                        myPaint.setTextSize(5f);
                        myPaint.setColor(Color.parseColor("#000000"));
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                        canvas.drawText("Hobbies", 10, MainY + 13.2f, myPaint);
                        MainY = MainY + 13.2f;
                        myPaint.setTextSize(3.5f);
                        MainY = MainY + 4f;
                        for (int i = 0; i < Awards.size(); i++) {
                            myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                            str = "•  " + Awards.get(i);
                            StaticLayout awards = StaticLayout.Builder.obtain(str, 0, str.length() - 1, myPaint, 192).build();
                            ObjectiveHeight = awards.getHeight();
                            canvas.translate(8, MainY);
                            awards.draw(canvas);
                            canvas.translate(-8, -MainY);
                            MainY = MainY + ObjectiveHeight + 2f;
                        }
                        MyPdfDocument.finishPage(myPage1);
                    } else {
                        MyPdfDocument.finishPage(myPage1);
                        PdfDocument.PageInfo myPageInfo2 = new PdfDocument.PageInfo.
                                Builder(210, 297, 2).create();
                        PdfDocument.Page myPage2 = MyPdfDocument.startPage(myPageInfo2);

                        Canvas canvas2 = myPage2.getCanvas();

                        myPaint.setColor(Color.parseColor("#D3D3D3"));
                        myPaint.setStrokeWidth(7f);
                        canvas2.drawLine(8f, 12, 202f, 12, myPaint);

                        myPaint.setTextAlign(Paint.Align.LEFT);

                        myPaint.setTextSize(5f);
                        myPaint.setColor(Color.parseColor("#000000"));
                        myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_semi), Typeface.NORMAL));
                        canvas2.drawText("Awards & Achievements", 10, 13, myPaint);
                        MainY = 13f;
                        myPaint.setTextSize(3.5f);
                        MainY = MainY + 4f;
                        for (int i = 0; i < Awards.size(); i++) {
                            myPaint.setTypeface(Typeface.create(String.valueOf(R.font.montserrat_normal), Typeface.NORMAL));
                            str = "•  " + Awards.get(i);
                            StaticLayout awards = StaticLayout.Builder.obtain(str, 0, str.length() - 1, myPaint, 192).build();
                            ObjectiveHeight = awards.getHeight();
                            canvas2.translate(8, MainY);
                            awards.draw(canvas2);
                            canvas2.translate(-8, -MainY);
                            MainY = MainY + ObjectiveHeight + 2f;
                        }
                        MyPdfDocument.finishPage(myPage2);
                    }

                    y = 17.368f;
                    MainY = 54.86f;
                    yR = y + 3.0f;
                    yL = y + 10.0f;


                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Lite.pdf");
                    try {
                        MyPdfDocument.writeTo(new FileOutputStream(file));
                        Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), PDFViewPage.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                    MyPdfDocument.close();
                }else{
                    Toast.makeText(getApplicationContext(),"Fill all the * fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkField() {
        if(!Mobile.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() && !Github.getText().toString().isEmpty()
        && !LinkedIn.getText().toString().isEmpty() && !discipline.getText().toString().isEmpty() && !branch.getText().toString().isEmpty()
        && !institute.getText().toString().isEmpty() && !collegeName.getText().toString().isEmpty() && !xSchool.getText().toString().isEmpty()
        && !xiiSchool.getText().toString().isEmpty() && !cCgpa.getText().toString().isEmpty() && !xCgpa.getText().toString().isEmpty()
        && !xiiCgpa.getText().toString().isEmpty() && !cYear.getText().toString().isEmpty() && !xYear.getText().toString().isEmpty()
        && !xiiYear.getText().toString().isEmpty() && !interest1.getText().toString().isEmpty() && !interest2.getText().toString().isEmpty()
        && !interest3.getText().toString().isEmpty() && !skillType1.getText().toString().isEmpty() && !skillType2.getText().toString().isEmpty()
        && !skillList1.getText().toString().isEmpty() && !skillList2.getText().toString().isEmpty() && !projectName1.getText().toString().isEmpty()
        && !projectPoint11.getText().toString().isEmpty() && !projectPoint21.getText().toString().isEmpty() && !projectPoint31.getText().toString().isEmpty()
        && !hobby1.getText().toString().isEmpty() && !hobby2.getText().toString().isEmpty() && !hobby3.getText().toString().isEmpty() && !award1.getText().toString().isEmpty()){
            return true;
        }else {
            return false;
        }

    }

    private void getTextField() {
        ValueDataR=new ArrayList<>();
        ValueDataR.add(Mobile.getText().toString());
        ValueDataR.add(Email.getText().toString());
        ValueDataR.add(Github.getText().toString());
        ValueDataR.add(LinkedIn.getText().toString());

        ValueDataL=new ArrayList<>();
        ValueDataL.add(discipline.getText().toString());
        ValueDataL.add(branch.getText().toString());
        ValueDataL.add(institute.getText().toString());

        eduInstitute=new ArrayList<>();
        eduInstitute.add(collegeName.getText().toString());
        eduInstitute.add(xSchool.getText().toString());
        eduInstitute.add(xiiSchool.getText().toString());

        eduCGPA=new ArrayList<>();
        eduCGPA.add(cCgpa.getText().toString());
        eduCGPA.add(xCgpa.getText().toString());
        eduCGPA.add(xiiCgpa.getText().toString());

        eduYear=new ArrayList<>();
        eduYear.add(xYear.getText().toString());
        eduYear.add(xYear.getText().toString());
        eduYear.add(xYear.getText().toString());

        areasOfInterest=new ArrayList<>();
        areasOfInterest.add(interest1.getText().toString());
        areasOfInterest.add(interest2.getText().toString());
        areasOfInterest.add(interest3.getText().toString());
        if(interest4.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: interest4 Empty ");
        }else{
            areasOfInterest.add(interest4.getText().toString());
        }
        if(interest5.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: interest5 Empty ");
        }else{
            areasOfInterest.add(interest5.getText().toString());
        }

        computerSkillKey=new ArrayList<>();
        computerSkillKey.add(skillType1.getText().toString());
        computerSkillKey.add(skillType2.getText().toString());

        computerSkillValue=new ArrayList<>();
        computerSkillValue.add(skillList1.getText().toString());
        computerSkillValue.add(skillList2.getText().toString());

        if(skillType3.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: skill3 empty");
        }else{
            computerSkillKey.add(skillType3.getText().toString());
            computerSkillValue.add(skillList3.getText().toString());
        }

        ProjectName=new ArrayList<>();
        ProjectName.add(projectName1.getText().toString());


        project0=new ArrayList<>();
        project0.add(projectPoint11.getText().toString());

        project1=new ArrayList<>();
        project1.add(projectPoint21.getText().toString());

        project2=new ArrayList<>();
        project2.add(projectPoint31.getText().toString());

        if(projectName2.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: Project 2 empty");
        }else{
            ProjectName.add(projectName2.getText().toString());
            project0.add(projectPoint12.getText().toString());
            project1.add(projectPoint22.getText().toString());
            project2.add(projectPoint32.getText().toString());
        }

        if(projectName3.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: Project 3 empty");
        }else{
            ProjectName.add(projectName3.getText().toString());
            project0.add(projectPoint13.getText().toString());
            project1.add(projectPoint32.getText().toString());
            project2.add(projectPoint33.getText().toString());
        }

        Hobbies=new ArrayList<>();
        Hobbies.add(hobby1.getText().toString());
        Hobbies.add(hobby2.getText().toString());
        Hobbies.add(hobby3.getText().toString());


        /*if(hobby4.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: hobby4 empty");
        }else{
            Hobbies.add(hobby4.getText().toString());
        }

        if(hobby5.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: hobby4 empty");
        }else{
            Hobbies.add(hobby5.getText().toString());
        }*/

        Awards=new ArrayList<>();
        Awards.add(award1.getText().toString());
        if(award2.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: award2 empty");
        }else{
            Awards.add(award2.getText().toString());
        }

        if(award3.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: award3 empty");
        }else{
            Awards.add(award3.getText().toString());
        }

        if(award4.getText().toString().isEmpty()){
            Log.i(TAG, "getTextField: award4 empty");
        }else{
            Awards.add(award4.getText().toString());
        }
    }
}