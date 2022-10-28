package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev334.litelo.Database.TinyDB;
import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.UI.home.eventAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EventActivity extends AppCompatActivity implements eventAdapter.ClickInterface{

    private RecyclerView timelineRecycler;
    private eventAdapter eventAdapter;
    private ExpandableTextView expendable_desc_tv;
    private TextView criteria_tv;

    private List<EventModel> Events;
    private FirebaseFirestore firestore;
    private List<Map<String, Object>> EventMap;
    private static String TAG="branchActivityLog";
    private String Branch;
    private Button subscribeBtn;
    private TinyDB tinyDB;
    private Boolean SUBSCRIBE=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        expendable_desc_tv.setText(getString(R.string.expandable_text));

        Branch=getIntent().getStringExtra("Branch");

        tinyDB=new TinyDB(this);

        SUBSCRIBE=tinyDB.getBoolean(Branch);

        subscribeBtn = findViewById(R.id.subscribeBtn);

        updateButton();

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SUBSCRIBE) {
                    SUBSCRIBE=false;
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Branch);
                }else{
                    SUBSCRIBE=true;
                    FirebaseMessaging.getInstance().subscribeToTopic(Branch);
                }

                updateButton();
            }
        });
/*
        firestore=FirebaseFirestore.getInstance();
        EventMap=new ArrayList<>();

        timelineRecycler=findViewById(R.id.timelineRecyclerView);
        Events=new ArrayList<>();

        fetchDataToday();

 */


    }

    private void setReferences() {
        // getting reference of  ExpandableTextView
        expendable_desc_tv = (ExpandableTextView) findViewById(R.id.branch_desc_tv).findViewById(R.id.branch_desc_tv);

        // calling setText on the ExpandableTextView so that
        // text content will be  displayed to the user
//        department = findViewById(R.id.department);
//        description = findViewById(R.id.description);
//        eventsRecycler = findViewById(R.id.eventsRecycler);
    }

    public void updateButton(){
        tinyDB.putBoolean(Branch, SUBSCRIBE);
        if(SUBSCRIBE){
            subscribeBtn.setBackground(getDrawable(R.drawable.grey_filled_box));
            subscribeBtn.setText("Subscribed");
        }else{
            subscribeBtn.setBackground(getDrawable(R.drawable.secondary_filled_box));
            subscribeBtn.setText("Subscribe");
        }
    }
/*
    private void fetchDataToday() {
        String test = "2021-12-19";
        firestore.collection("Events").document(Branch)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
//                    branchDesc.setText(documentSnapshot.get("Desc").toString());
                    EventMap = (List<Map<String, Object>>) documentSnapshot.get("Events");
                    if(EventMap==null) {
                        return;
                    }
                    EventMap.sort(new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                            return m1.get("Date").toString().compareTo(m2.get("Date").toString());
                        }
                    });
                    Events = EventMap.stream().map(MapToEvents).collect(Collectors.<EventModel>toList());
                    setUpRecycler();
                    //sortEventModelList();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
*/
    /*
    private boolean sortEventModelList() {
        Events.sort(new Comparator<EventModel>() {
            @Override
            public int compare(EventModel e1, EventModel e2) {
                Integer d1 = Integer.parseInt(e1.getDate().substring(8));
                Integer d2 = Integer.parseInt(e2.getDate().substring(8));

                Log.i(TAG, "compare: " + d1 + " " + d2);

                if (d1 > d2) {
                    Log.i(TAG, "compare: d1>d2");
                    return 0;
                } else if (d1 < d2) {
                    Log.i(TAG, "compare: d1<d2");
                    return 1;
                }


                Integer h1 = Integer.parseInt(e1.getTime().substring(0, 2));
                Integer h2 = Integer.parseInt(e2.getTime().substring(0, 2));

                if (h1 > h2) {
                    Log.i(TAG, "compare: d1>d2");
                    return 0;
                } else if (h1 < h2) {
                    Log.i(TAG, "compare: d1<d2");
                    return 1;
                }

                Integer m1 = Integer.parseInt(e1.getTime().substring(3));
                Integer m2 = Integer.parseInt(e2.getTime().substring(3));
                if (m1 >= m2) {
                    Log.i(TAG, "compare: d1>d2");
                    return 0;
                } else {
                    Log.i(TAG, "compare: d1<d2");
                    return 1;
                }
            }
        });

        eventAdapter.notifyDataSetChanged();

        return true;
    }
*/
    /*
    private void setUpRecycler() {
        eventAdapter=new eventAdapter(Events, EventActivity.this);
        timelineRecycler.setAdapter(eventAdapter);
        timelineRecycler.setLayoutManager(new LinearLayoutManager(getApplication()));
        timelineRecycler.setHasFixedSize(true);
    }

     */

    Function<Map<String, Object>, EventModel> MapToEvents = new Function<Map<String, Object>, EventModel>() {
        @Override
        public EventModel apply(Map<String, Object> stringObjectMap) {
            EventModel event = new EventModel(stringObjectMap);
            return event;
        }
    };

    @Override
    public void eventViewOnClick(int position) {

    }
}