package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private CardView addEvent, removeEvent, sendNotification;
    private String branch;

    private RequestQueue mQueue;
    private String fcmUrl="https://fcm.googleapis.com/fcm/send";

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
        AlertDialog.Builder alert=new AlertDialog.Builder(getApplicationContext());
        View view=getLayoutInflater().inflate(R.layout.dialog_add_event,null);

        TextView eName, eDate, eTime, eDesc, eLink, eCord1, eCord1P, eCord2, eCord2P;
        Button DoneBtn;

        eName=view.findViewById(R.id.addEvent_Name);
        DoneBtn=view.findViewById(R.id.addEvent_Btn);


        alert.setView(view);
        AlertDialog show=alert.show();

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                //done
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
    
}