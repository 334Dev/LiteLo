package com.dev334.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        firestore=FirebaseFirestore.getInstance();
        EventMap=new ArrayList<>();

        timelineRecycler=findViewById(R.id.timelineRecyclerView);
        Events=new ArrayList<>();

        fetchDataToday();



    }

    private void fetchDataToday() {
        String test = "2021-12-19";
        firestore.collection("Events").document(test)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EventMap= (List<Map<String, Object>>) documentSnapshot.get("Events");
                Events=EventMap.stream().map(MapToEvents).collect(Collectors.<EventModel> toList());
                Log.i(TAG, "onSuccess: "+Events.get(0).getName());
                setUpRecycler();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
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