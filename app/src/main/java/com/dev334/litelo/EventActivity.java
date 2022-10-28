package com.dev334.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.litelo.model.TimelineModel;
import com.dev334.litelo.UI.home.TimelineAdapter;
import com.dev334.litelo.utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

public class EventActivity extends AppCompatActivity implements TimelineAdapter.ClickInterface {

    private ExpandableTextView expendable_desc_tv;
    private TextView event_tv, criteria_tv, problem_stat_tv;
    private RecyclerView timelineRecycler;
    private TimelineAdapter timelineAdapter;
    private com.dev334.litelo.model.EventModel eventModel;
    private SharedPreferences preferences;
    private Button subscribeBtn;
    private Boolean subscribed = false;

    private List<TimelineModel> timelineModels = new ArrayList<>();
    private FirebaseFirestore fireStore;
    private List<Map<String, Object>> EventMap = new ArrayList<>();
    private static String TAG = "branchActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setReferences();
        setValues();
        fetchTimeline();
    }

    private void setReferences() {
        expendable_desc_tv = findViewById(R.id.branch_desc_tv).findViewById(R.id.branch_desc_tv);
        event_tv = findViewById(R.id.branchEvent_textView);
        criteria_tv = findViewById(R.id.criteria_content_tv);
        eventModel = (com.dev334.litelo.model.EventModel) getIntent().getSerializableExtra(Constants.EVENT);
        subscribeBtn = findViewById(R.id.subscribeBtn);
        problem_stat_tv = findViewById(R.id.problem_statement_tv);
        timelineRecycler = findViewById(R.id.timelineRecyclerView);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        fireStore = FirebaseFirestore.getInstance();
    }

    private void setValues() {
        timelineRecycler.setNestedScrollingEnabled(false);
        if (eventModel == null) return;
        event_tv.setText(eventModel.getName());
        expendable_desc_tv.setText(Html.fromHtml(eventModel.getDetails(), Html.FROM_HTML_MODE_COMPACT));
        criteria_tv.setText(Html.fromHtml(eventModel.getCriteria(), Html.FROM_HTML_MODE_COMPACT));
        subscribed = preferences.getBoolean(eventModel.getName(), false);
        updateButton();
        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeBtn.setEnabled(false);
                if (subscribed) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(eventModel.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            subscribed = false;
                            updateSubscription();
                            updateButton();
                        }
                    });
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic(eventModel.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        problem_stat_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = eventModel.getPsLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void updateSubscription() {
        Map<String, Object> map = new HashMap<>();
        map.put(eventModel.getName(), subscribed);
        FirebaseFirestore.getInstance()
                .collection(Constants.SUBSCRIPTIONS)
                .document(preferences.getString(Constants.EMAIL, ""))
                .set(map, SetOptions.merge());
    }

    public void updateButton() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(eventModel.getName(), subscribed);
        editor.apply();
        subscribeBtn.setEnabled(true);
        if (subscribed) {
            subscribeBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_filled_box));
            subscribeBtn.setText("Unsubscribe");
        } else {
            subscribeBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.secondary_filled_box));
            subscribeBtn.setText("Subscribe");
        }
    }

    private void fetchTimeline() {
        fireStore
                .collection("Timeline")
                .document(getIntent().getStringExtra(Constants.DEPARTMENT))
                .collection(eventModel.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments())
                                timelineModels.add(documentSnapshot.toObject(TimelineModel.class));
                            timelineModels.sort(new Comparator<TimelineModel>() {
                                @Override
                                public int compare(TimelineModel o1, TimelineModel o2) {
                                    Calendar c1 = getCalendar(o1);
                                    Calendar c2 = getCalendar(o2);
                                    if (c1.getTimeInMillis() > c2.getTimeInMillis()) return 1;
                                    if (c1.getTimeInMillis() < c2.getTimeInMillis()) return -1;
                                    return 0;
                                }

                                private Calendar getCalendar(TimelineModel o2) {
                                    StringTokenizer dateTokenizer = new StringTokenizer(o2.getDate(), "-");
                                    StringTokenizer timeTokenizer = new StringTokenizer(o2.getTime(), ":");
                                    Calendar c = Calendar.getInstance();
                                    c.set(
                                            Integer.parseInt(dateTokenizer.nextToken()),
                                            Integer.parseInt(dateTokenizer.nextToken()),
                                            Integer.parseInt(dateTokenizer.nextToken()),
                                            Integer.parseInt(timeTokenizer.nextToken()),
                                            Integer.parseInt(timeTokenizer.nextToken()));
                                    return c;
                                }
                            });
                            setUpRecycler();
                        } else {
                            if (task.getException() != null)
                                Log.i(TAG, "onFailure: " + task.getException().getMessage());
                            else
                                Toast.makeText(EventActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setUpRecycler() {
        timelineAdapter = new TimelineAdapter(timelineModels, EventActivity.this);
        timelineRecycler.setAdapter(timelineAdapter);
        timelineRecycler.setLayoutManager(new LinearLayoutManager(getApplication()));
        timelineRecycler.setHasFixedSize(true);
    }


    Function<Map<String, Object>, TimelineModel> MapToEvents = new Function<Map<String, Object>, TimelineModel>() {
        @Override
        public TimelineModel apply(Map<String, Object> stringObjectMap) {
            TimelineModel event = new TimelineModel(stringObjectMap);
            return event;
        }
    };

    @Override
    public void eventViewOnClick(int position) {

    }
}