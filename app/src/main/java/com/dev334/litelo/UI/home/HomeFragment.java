package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.resourceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment implements todayAdapter.ClickInterface, branchAdapter.ClickInterface, filterAdapter.ClickInterface{

    private static final String TAG ="HomeFragment" ;
    private HomeViewModel homeViewModel;
    private List<EventModel> Events, filterEvents;
    private RecyclerView todayRecycler;
    private RecyclerView branchRecycler;
    private todayAdapter AdapterToday;
    private branchAdapter AdapterBranch;
    private filterAdapter AdapterFilter;
    private RecyclerView filterRecycler;
    private List<Map<String,String>> branchList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances

        Events=new ArrayList<>();
        filterEvents = new ArrayList<>();
        branchList =new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        map.put("Name","Cyberquest");
        Events=((HomeActivity)getActivity()).getEvents();
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));

        branchList.add(map);
        branchList.add(map);
        branchList.add(map);
        branchList.add(map);
        branchList.add(map);
        branchList.add(map);
        branchList.add(map);

        todayRecycler=root.findViewById(R.id.todayEventRecycler);
        branchRecycler=root.findViewById(R.id.recyclerView2);
        filterRecycler=root.findViewById(R.id.filterEventRecycler);
        filterRecycler.setNestedScrollingEnabled(false);
        setupBranchRecycler();
        setupTodayRecycler();
        setupFilterRecycler();

        return root;
    }

    private void setupFilterRecycler() {
        AdapterFilter= new filterAdapter(Events,this);
        filterRecycler.setAdapter(AdapterFilter);
        filterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        filterRecycler.setHasFixedSize(true);
    }

    private void setupBranchRecycler() {

        AdapterBranch= new branchAdapter(branchList,this);
        branchRecycler.setAdapter(AdapterBranch);
        branchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        branchRecycler.setHasFixedSize(true);
    }
    private void setupTodayRecycler() {

        AdapterToday= new todayAdapter(Events,this);
        todayRecycler.setAdapter(AdapterToday);
        todayRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        todayRecycler.setHasFixedSize(true);
    }

    @Override
    public void recyclerviewOnClick(int position) {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.event_dailog,null);
        TextView link=view.findViewById(R.id.textView19);
        TextView website=view.findViewById(R.id.textView20);
        website.setMovementMethod(LinkMovementMethod.getInstance());
        alert.setView(view);
        AlertDialog show=alert.show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void branchviewOnClick(int position) {

    }

    @Override
    public void filterViewOnClick(int position) {

    }
}
