package com.dev334.litelo;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private LinearLayout addEvent, sendNotification;
    private String branch;

    private RequestQueue mQueue;
    private String fcmUrl="https://fcm.googleapis.com/fcm/send";

    private TextView eName, eDate, eTime, eDesc, eLink, eCord1, eCord1P, eCord2, eCord2P;
    private Button DoneBtn,PickDate,PickTime;
    private List<Map<String, Object>> events;
    private Map<String, Object> fMap;
    private FirebaseFirestore firestore;
    private static String TAG="AdminActivityLog";
    private Map<String, Object> map;
   
    int hour,min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addEvent=findViewById(R.id.admin_addEvent);
        sendNotification=findViewById(R.id.admin_notification);
        firestore=FirebaseFirestore.getInstance();
        
        //branch name
        branch=getIntent().getStringExtra("Branch");
        events=new ArrayList<>();

        mQueue= Volley.newRequestQueue(this);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventCard();
            }
        });


        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: sendNotification");
                showNotificationDialog();
            }
        });

    }

    private void showNotificationDialog() {
        AlertDialog.Builder alert=new AlertDialog.Builder(AdminActivity.this);
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
                show.dismiss();
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                String Name=eName.getText().toString();
                String Date=eDate.getText().toString();
                String Link=eLink.getText().toString();
                String Desc=eDesc.getText().toString();
                String Time=eTime.getText().toString();
                String cord1=eCord1.getText().toString();
                String cord2=eCord2.getText().toString();
                String cord1P=eCord1P.getText().toString();
                String cord2P=eCord2P.getText().toString();

                //add Data to firebase;
                map=new HashMap<>();
                fMap=new HashMap<>();
                map.put("Name", Name);
                map.put("Date", Date);
                map.put("Time", Time);
                map.put("Desc", Desc);
                map.put("Link", Link);
                map.put("Parent", branch);
                Map<String, Object> cMap=new HashMap<>();
                cMap.put(cord1, cord1P);
                cMap.put(cord2, cord2P);

                map.put("Coordinator", cMap);

                Log.i(TAG, "onClick: "+map);
                firestore.collection("Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            events=(List<Map<String, Object>>) documentSnapshot.get("Events");
                        }
                        assert events != null;
                        events.add(map);
                        Log.i(TAG, "onSuccess: "+events);
                        fMap.put("Events", events);

                        addEventDate(Date);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });


                show.dismiss();


            }
        });


        closeAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                show.dismiss();

            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void addEventDate(String date) {
        firestore.collection("Events").document(date).set(fMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i(TAG, "onSuccess: added");
                addEventParent();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void addEventParent() {
        events.clear();
        fMap.clear();
        firestore.collection("Events").document(branch).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Desc="Avishkar event organiser";
                if(documentSnapshot.exists()) {
                    events = (List<Map<String, Object>>) documentSnapshot.get("Events");
                    Desc= (String) documentSnapshot.get("Desc");
                }
                events.add(map);
                fMap.put("Events", events);
                fMap.put("Desc", Desc);
                addEventToParent();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void addEventToParent() {
        firestore.collection("Events").document(branch).set(fMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Done
                        Toast.makeText(AdminActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void sendNotification(String descText) {

        //Making a Json Object
        JSONObject mainobj=new JSONObject();
        try {
            mainobj.put("to","/topics/"+branch);
            JSONObject notificationObj=new JSONObject();
            notificationObj.put("title","Avishkar: "+branch);
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
                    Log.i(TAG, "onErrorResponse: "+error.getMessage());
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
            Log.i(TAG, "sendNotification: "+e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=month+1;
        Log.i("DateSelectedAdmin", "onDateSet: "+year+" "+month+" "+dayOfMonth);
        String date=year+"-"+month+"-"+dayOfMonth;
        eDate.setText(date);
    }
}