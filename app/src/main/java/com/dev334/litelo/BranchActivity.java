package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.UI.home.eventAdapter;
import com.dev334.litelo.UI.home.filterAdapter;
import com.dev334.litelo.UI.home.todayAdapter;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity implements eventAdapter.ClickInterface{

    private RecyclerView timelineRecycler;
    private eventAdapter eventAdapter;
    private List<EventModel> Events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        timelineRecycler=findViewById(R.id.timeline);
        Events=new ArrayList<>();
        Events=((HomeActivity)getApplicationContext()).getEvents();
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));

        eventAdapter=new eventAdapter(Events,this);
        timelineRecycler.setAdapter(eventAdapter);
        timelineRecycler.setLayoutManager(new LinearLayoutManager(getApplication()));
        timelineRecycler.setHasFixedSize(true);

    }

    @Override
    public void eventViewOnClick(int position) {

    }
}