package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private CardView addEvent, removeEvent, sendNotification;
    private String branch;

    private RequestQueue mQueue;
    private String fcmUrl="https://fcm.googleapis.com/fcm/send";

    TextView eName, eDate, eTime, eDesc, eLink, eCord1, eCord1P, eCord2, eCord2P;
    Button DoneBtn,PickDate,PickTime;
   
    int hour,min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addEvent=findViewById(R.id.addEvent);
        removeEvent=findViewById(R.id.removeEvent);
        sendNotification=findViewById(R.id.sendNotification);
        
        //branch name
        branch="Cyberquest";

        mQueue= Volley.newRequestQueue(this);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventCard();
            }
        });

        removeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getApplicationContext());
                View view=getLayoutInflater().inflate(R.layout.dialog_send_notification,null);

                TextView EditDesc= view.findViewById(R.id.notificationDesc);
                Button sendBtn=view.findViewById(R.id.sendNotiBtn);

                alert.setView(view);
                AlertDialog show=alert.show();

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vi) {
                        if(EditDesc.getText().toString().isEmpty()){
                            EditDesc.setError("Empty Description");
                        }else{
                            //send Notification
                            sendNotification(EditDesc.getText().toString());
                        }
                    }
                });

                alert.setCancelable(true);
                show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

    }

    private void showAddEventCard() {
        AlertDialog.Builder alert=new AlertDialog.Builder(AdminActivity.this);
        View view=getLayoutInflater().inflate(R.layout.dialog_add_event,null);



        eName=view.findViewById(R.id.addEvent_Name);
        eDate=view.findViewById(R.id.addEvent_date);
        eTime=view.findViewById(R.id.addEvent_time);
        eDesc=view.findViewById(R.id.addEvent_Desc);
        eLink=view.findViewById(R.id.addEvent_Link);
        eCord1=view.findViewById(R.id.addEvent_c1);
        eCord1P=view.findViewById(R.id.addEvent_c1p);
        eCord2=view.findViewById(R.id.addEvent_c2);
        eCord2P=view.findViewById(R.id.addEvent_c2p);
        DoneBtn=view.findViewById(R.id.addEvent_Btn);
        PickDate=view.findViewById(R.id.addEvent_Date);
        PickTime=view.findViewById(R.id.addEvent_Time);
        ImageView closeAlert=view.findViewById(R.id.addEvent_close);

        alert.setView(view);
        AlertDialog show=alert.show();

         PickDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 DialogFragment datePicker= new DatePickerFragment();
                 datePicker.show(getSupportFragmentManager(), "class date picker");

             }
         });

         PickTime.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker timePicker, int selectedhour, int selectedmin) {

                         hour=selectedhour;
                         min=selectedmin;

                       eTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,min));

                     }
                 };
                 TimePickerDialog timePickerDialog =new TimePickerDialog(AdminActivity.this,onTimeSetListener,hour,min,true);
                 timePickerDialog.show();

             }
         });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {


 


            }
        });


        closeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                alert.dismiss();

            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void sendNotification(String descText) {

        //Making a Json Object
        JSONObject mainobj=new JSONObject();
        try {
            mainobj.put("to","/topics/"+branch);
            JSONObject notificationObj=new JSONObject();
            notificationObj.put("title",branch);
            notificationObj.put("body",descText);
            // notificationObj.put("click_action","https://www.google.com/");
            mainobj.put("notification",notificationObj);

            JsonObjectRequest request=new JsonObjectRequest(com.android.volley.Request.Method.POST, fcmUrl,
                    mainobj,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new  com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA_hs-taw:APA91bGL8jhmZOv6EMcTppUww6QWQyEvJLcVWW9R-aXa4h18f8AIRYddyRMJCLOtg5ySkc-Ol5XotfSi01x37j-F4LP9p60FbRG9yz_0-LtgJTKCfbencC-Kq7xHYDBpa_P4YAxISiIJ");
                    return header;
                }
            };

            mQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        eDate.setText(currentDate);
    }
}