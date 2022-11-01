package com.dev334.litelo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev334.litelo.model.AdminModel;
import com.dev334.litelo.model.NotificationModel;
import com.dev334.litelo.model.TimelineModel;
import com.dev334.litelo.utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private LinearLayout addEvent, sendNotification;
    private String branch;

    private RequestQueue mQueue;
    private final String fcmUrl = "https://fcm.googleapis.com/fcm/send";
    private List<AdminModel> adminModels = new ArrayList<>();
    private TextView eDate, eTime, eDesc, eLink;
    private ProgressBar progressBar;
    private Button DoneBtn, PickDate, PickTime;
    private List<Map<String, Object>> events;
    private FirebaseFirestore firestore;
    private static String TAG = "AdminActivityLog";
    private ArrayList<AdminModel> adminModel = new ArrayList<>();
    int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        addEvent = findViewById(R.id.admin_addEvent);
        sendNotification = findViewById(R.id.admin_notification);
        firestore = FirebaseFirestore.getInstance();

        // Admin of
        String adminOf = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE).getString(Constants.ADMIN, "");
        Gson gson = new GsonBuilder().create();
        adminModels = gson.fromJson(adminOf, new TypeToken<ArrayList<AdminModel>>() {
        }.getType());

        //branch name
        branch = getIntent().getStringExtra("Branch");
        events = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);

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
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_send_notification, null);

        Spinner eventSpinner = view.findViewById(R.id.event_spinner);
        TextView EditDesc = view.findViewById(R.id.notificationDesc);
        Button sendBtn = view.findViewById(R.id.sendNotiBtn);

        ArrayAdapter<AdminModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);
        eventSpinner.setOnItemSelectedListener(this);

        alert.setView(view);
        AlertDialog show = alert.show();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                if (EditDesc.getText().toString().isEmpty()) {
                    EditDesc.setError("Empty Description");
                } else {
                    sendNotification(show, (AdminModel) eventSpinner.getSelectedItem(), EditDesc.getText().toString());
                }
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void showAddEventCard() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AdminActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_event, null);

        Spinner eventSpinner = view.findViewById(R.id.event_spinner);
        eDate = view.findViewById(R.id.addEvent_date);
        eTime = view.findViewById(R.id.addEvent_time);
        eDesc = view.findViewById(R.id.addEvent_Desc);
        eLink = view.findViewById(R.id.addEvent_Link);
        DoneBtn = view.findViewById(R.id.addEvent_Btn);
        PickDate = view.findViewById(R.id.addEvent_Date);
        PickTime = view.findViewById(R.id.addEvent_Time);
        progressBar = view.findViewById(R.id.progress_circular);
        ImageView closeAlert = view.findViewById(R.id.addEvent_close);

        ArrayAdapter<AdminModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);
        eventSpinner.setOnItemSelectedListener(this);

        alert.setView(view);
        AlertDialog show = alert.show();

        PickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "class date picker");
            }
        });

        PickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedhour, int selectedmin) {
                        hour = selectedhour;
                        min = selectedmin;
                        eTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, min));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(AdminActivity.this, onTimeSetListener, hour, min, true);
                timePickerDialog.show();
            }
        });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                progressBar.setVisibility(View.VISIBLE);
                DoneBtn.setVisibility(View.GONE);
                SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                AdminModel selected = (AdminModel) eventSpinner.getSelectedItem();
                String Name = selected.getEvent();
                String Date = eDate.getText().toString();
                String Link = eLink.getText().toString();
                String Desc = eDesc.getText().toString();
                String Time = eTime.getText().toString();

                //add Data to firebase;
                Map<String, Object> map = new HashMap<>();
                map.put("name", Name);
                map.put("date", Date);
                map.put("time", Time);
                map.put("desc", Desc);
                map.put("link", Link);
                map.put("parent", selected.getDeptId());

                firestore.collection("Timeline")
                        .document("Events")
                        .collection(selected.getEventId())
                        .add(new TimelineModel(map))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    addDateWiseEvent(map, show);
                                } else {
                                    Toast.makeText(AdminActivity.this, "Event could not be added", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    DoneBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
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

    private void addDateWiseEvent(Map<String, Object> map, AlertDialog show) {
        Map<String, List<Map<String, Object>>> updated = new HashMap<>();
        firestore.collection("DateWiseEvent")
                .document((String) Objects.requireNonNull(map.get("date")))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() || task.getResult() == null) {
                            List<Map<String, Object>> list = new ArrayList<>();
                            if (task.getResult() != null && task.getResult().get("Events") != null) {
                                list.addAll((List<Map<String, Object>>) task.getResult().get("Events"));
                            }
                            list.add(map);
                            updated.put("Events", list);
                            FirebaseFirestore.getInstance()
                                    .collection("DateWiseEvent")
                                    .document((String) Objects.requireNonNull(map.get("date")))
                                    .set(updated)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            show.dismiss();
                                            Toast.makeText(AdminActivity.this, "Event added successfully", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        show.dismiss();
                        Toast.makeText(AdminActivity.this, "Event added successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendNotification(AlertDialog show, AdminModel model, String descText) {

        //Making a Json Object
        JSONObject mainobj = new JSONObject();
        try {
            mainobj.put("to", "/topics/" + model.getEventId());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", model.getEvent());
            notificationObj.put("eventId", model.getEventId());
            notificationObj.put("deptId", model.getDeptId());
            notificationObj.put("body", descText);
            notificationObj.put("time", System.currentTimeMillis());
            mainobj.put("data", notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, fcmUrl,
                    mainobj,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA_hs-taw:APA91bGL8jhmZOv6EMcTppUww6QWQyEvJLcVWW9R-aXa4h18f8AIRYddyRMJCLOtg5ySkc-Ol5XotfSi01x37j-F4LP9p60FbRG9yz_0-LtgJTKCfbencC-Kq7xHYDBpa_P4YAxISiIJ");
                    return header;
                }
            };
            mQueue.add(request);
            addNotificationToFirebase(show, notificationObj, model.getEventId());
        } catch (JSONException e) {
            Log.i(TAG, "sendNotification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addNotificationToFirebase(AlertDialog show, JSONObject notificationObj, String eventId) {
        FirebaseFirestore.getInstance()
                .collection("Notifications")
                .document("Notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<NotificationModel> notifications = new ArrayList<>();
                            if (task.getResult() != null && task.getResult().get("Notifications") != null)
                                notifications.addAll((List<NotificationModel>) task.getResult().get("Notifications"));
                            try {
                                notifications.add(new NotificationModel(
                                        eventId,
                                        notificationObj.getString("title"),
                                        notificationObj.getString("body"),
                                        notificationObj.getLong("time")));
                            } catch (JSONException exception) {
                                exception.printStackTrace();
                            }
                            Map<String, List<NotificationModel>> mp = new HashMap<>();
                            mp.put("Notifications", notifications);
                            FirebaseFirestore.getInstance()
                                    .collection("Notifications")
                                    .document("Notifications")
                                    .set(mp)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(AdminActivity.this, "Sent", Toast.LENGTH_LONG).show();
                                            else

                                                Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_LONG).show();
                                            show.dismiss();
                                        }
                                    });
                        } else {
                            Toast.makeText(AdminActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            show.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        Log.i("DateSelectedAdmin", "onDateSet: " + year + " " + month + " " + dayOfMonth);
        String date = year + "-" + month + "-" + dayOfMonth;
        eDate.setText(date);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}