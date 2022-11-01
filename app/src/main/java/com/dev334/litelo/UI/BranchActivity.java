package com.dev334.litelo.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.EventActivity;
import com.dev334.litelo.EventAdapter;
import com.dev334.litelo.R;
import com.dev334.litelo.model.DepartmentModel;
import com.dev334.litelo.model.EventModel;
import com.dev334.litelo.model.EventRequest;
import com.dev334.litelo.model.EventResponse;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchActivity extends AppCompatActivity {

    private TextView department, description;
    private RecyclerView eventsRecycler;
    private EventAdapter adapter;
    private DepartmentModel departmentModel;
    private Button subscribeBtn;
    private Boolean subscribed = false;
    private List<EventModel> events = new ArrayList<>();
    private SharedPreferences preferences;
    private ProgressBar subscribeProgress, dataProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setReferences();
        setValues();
        fetchEvents();
    }

    private void setReferences() {
        department = findViewById(R.id.department);
        description = findViewById(R.id.description);
        eventsRecycler = findViewById(R.id.eventsRecycler);
        subscribeBtn = findViewById(R.id.subscribeBtn);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        subscribeProgress = findViewById(R.id.subscribeProgress);
        dataProgress = findViewById(R.id.data_progress);
    }

    private void setValues() {
        departmentModel = (DepartmentModel) getIntent().getSerializableExtra(Constants.DEPARTMENT);
        if (departmentModel == null)
            finish();
        subscribed = preferences.getBoolean(departmentModel.getName().toLowerCase(Locale.ROOT), false);
        updateButton();
        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeBtn.setVisibility(View.GONE);
                subscribeProgress.setVisibility(View.VISIBLE);
                if (subscribed) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(departmentModel.getName().toLowerCase(Locale.ROOT)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            subscribed = false;
                            updateSubscription();
                            updateButton();
                        }
                    });
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic(departmentModel.getName().toLowerCase(Locale.ROOT)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            subscribed = true;
                            updateSubscription();
                            updateButton();
                        }
                    });
                }
            }
        });
    }

    private void updateSubscription() {
        Map<String, Object> map = new HashMap<>();
        map.put(departmentModel.getName().toLowerCase(Locale.ROOT), subscribed);
        FirebaseFirestore.getInstance()
                .collection(Constants.SUBSCRIPTIONS)
                .document(preferences.getString(Constants.EMAIL, ""))
                .set(map, SetOptions.merge());
    }

    public void updateButton() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(departmentModel.getName().toLowerCase(Locale.ROOT), subscribed);
        editor.apply();
        subscribeBtn.setVisibility(View.VISIBLE);
        subscribeProgress.setVisibility(View.GONE);
        if (subscribed) {
            subscribeBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_filled_box));
            subscribeBtn.setText("Unsubscribe");
        } else {
            subscribeBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.secondary_filled_box));
            subscribeBtn.setText("Subscribe");
        }
    }

    private void fetchEvents() {
        department.setText(departmentModel.getName());
        description.setText(departmentModel.getDesc());
        EventRequest request = new EventRequest(departmentModel.getId());
        RetrofitAccessObject.
                getRetrofitAccessObject()
                .getEvents(request)
                .enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getEvents() != null)
                                events = response.body().getEvents();
                            dataProgress.setVisibility(View.GONE);
                            setUpAdapter();
                        } else {
                            dataProgress.setVisibility(View.GONE);
                            Toast.makeText(BranchActivity.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {

                    }
                });
    }

    private void setUpAdapter() {
        adapter = new EventAdapter(events, this, departmentModel.getName());
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsRecycler.setAdapter(adapter);
    }
}