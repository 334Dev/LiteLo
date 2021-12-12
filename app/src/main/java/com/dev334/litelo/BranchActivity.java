package com.dev334.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.UI.home.eventAdapter;
import com.dev334.litelo.UI.home.filterAdapter;
import com.dev334.litelo.UI.home.todayAdapter;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BranchActivity extends AppCompatActivity implements eventAdapter.ClickInterface{

    private RecyclerView timelineRecycler;
    private eventAdapter eventAdapter;
    private List<EventModel> Events;
    private FirebaseFirestore firestore;
    private List<Map<String, Object>> EventMap;
    private static String TAG="branchActivityLog";
    private String Branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        Branch=getIntent().getStringExtra("Branch");

        firestore=FirebaseFirestore.getInstance();
        EventMap=new ArrayList<>();

        timelineRecycler=findViewById(R.id.timelineRecyclerView);
        Events=new ArrayList<>();

        fetchDataToday();



    }

    private void fetchDataToday() {
        String test = "2021-12-19";
        firestore.collection("Events").document(Branch)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    EventMap = (List<Map<String, Object>>) documentSnapshot.get("Events");
                    Events = EventMap.stream().map(MapToEvents).collect(Collectors.<EventModel>toList());
                    Log.i(TAG, "onSuccess: " + Events.get(0).getName());
                    sortEventModelList(Events);
                    setUpRecycler();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void sortEventModelList(List<EventModel> events) {
        events.sort(new Comparator<EventModel>() {
            @Override
            public int compare(EventModel e1, EventModel e2) {
                Integer h1=Integer.parseInt(e1.getTime().substring(0,2));
                Integer h2=Integer.parseInt(e2.getTime().substring(0,2));

                if(h1>h2){
                    return 1;
                }else if(h1<h2){
                    return 0;
                }

                Integer m1=Integer.parseInt(e1.getTime().substring(3));
                Integer m2=Integer.parseInt(e2.getTime().substring(3));
                if(m1>=m2){
                    return 1;
                }else{
                    return 0;
                }

            }
        });
    }

    private void setUpRecycler() {
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        eventAdapter=new eventAdapter(Events,BranchActivity.this);
        timelineRecycler.setAdapter(eventAdapter);
        timelineRecycler.setLayoutManager(new LinearLayoutManager(getApplication()));
        timelineRecycler.setHasFixedSize(true);
    }

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