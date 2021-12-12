package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.dev334.litelo.BranchActivity;
import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.resourceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment implements todayAdapter.ClickInterface, branchAdapter.ClickInterface, filterAdapter.ClickInterface{

    private static final String TAG ="HomeFragment" ;
    private HomeViewModel homeViewModel;
    private List<EventModel> Events, filterEvents;
    private RecyclerView todayRecycler;
    private RecyclerView branchRecycler;
    private RecyclerView filterRecycler;
    private todayAdapter AdapterToday;
    private branchAdapter AdapterBranch;
    private filterAdapter AdapterFilter;
    private List<Map<String,Object>> branchList;

    public HomeFragment(){
        //empty constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances

        Events=new ArrayList<>();
        filterEvents = new ArrayList<>();
        branchList =new ArrayList<>();


        String[] branch_names = new String[]{
              "Cyberquest","Oligopoly","Techno Art","Rasayans","Kreedomania","Monopoly","Nirmaan","Astrowing","Powersurge","Mecrocosm","Robomania",
                "Aerodynamix","Genesis","Electromania","Gnosiomania"
        };

        Integer[] branch_logo=new Integer[]{
                R.drawable.ic_branch_logo_cyberquest_01,
                R.drawable.ic_branch_logo_oligopoly_01,
                R.drawable.ic_branch_logo_technoart_01,
                R.drawable.ic_branch_logo_rasayan_01,
                R.drawable.ic_branch_logo_kreedomania_01,
                R.drawable.ic_branch_logo_monopoly_01,
                R.drawable.ic_nirmaan,
                R.drawable.ic_branh_logo_aero_01,
                R.drawable.ic_branch_logo_powersurge_01,
                R.drawable.ic_branch_logo_01,
                R.drawable.ic_branch_logo_robo_01,
                R.drawable.ic_branh_logo_aero_01,
                R.drawable.ic_genesis,
                R.drawable.ic_branch_logo_electromania_01,
                R.drawable.ic_branch_logo_gnosomania_01,


        };

        for(int i=0;i<15;i++)
        {
              Map<String,Object> map=new HashMap<>();
              map.put("Name",branch_names[i]);
              map.put("Img",branch_logo[i]);
              branchList.add(map);

        }

        Events=((HomeActivity)getActivity()).getEvents();
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));
        Events.add(Events.get(0));


        filterEvents=((HomeActivity)getActivity()).getTomorrowEvents();
        filterEvents.add(filterEvents.get(0));
        filterEvents.add(filterEvents.get(0));
        filterEvents.add(filterEvents.get(0));
        filterEvents.add(filterEvents.get(0));

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
        AdapterFilter= new filterAdapter(filterEvents,this);
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

        AdapterToday= new todayAdapter(Events,this,getContext());
        todayRecycler.setAdapter(AdapterToday);
        todayRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        todayRecycler.setHasFixedSize(true);
    }

    @Override
    public void recyclerviewOnClick(int position) {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.event_dailog,null);
        TextView link=view.findViewById(R.id.textView19);
        TextView event=view.findViewById(R.id.event_dialog_name);
        event.setText(Events.get(position).getName());
        TextView website=view.findViewById(R.id.textView20);
        website.setMovementMethod(LinkMovementMethod.getInstance());
        alert.setView(view);
        AlertDialog show=alert.show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Events.get(position).getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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
       Intent intent=new Intent(getActivity(), BranchActivity.class);
       intent.putExtra("Branch", String.valueOf(branchList.get(position).get("Name")));
       startActivity(intent);
    }

    @Override
    public void filterViewOnClick(int position) {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.event_full_detail,null);
        alert.setView(view);
        AlertDialog show=alert.show();

        TextView evtName,evtDesc,cord1Name,cord2Name,cord1Phone,cord2Phone,linkMain;
        evtName=view.findViewById(R.id.event_name_full);
        evtDesc=view.findViewById(R.id.event_desc_full);
        cord1Name=view.findViewById(R.id.coordinator_name_full);
        cord2Name=view.findViewById(R.id.coordinator_name_full2);
        cord1Phone=view.findViewById(R.id.coordinator_number_full);
        cord2Phone=view.findViewById(R.id.coordinator_number_full2);
        linkMain =view.findViewById(R.id.website_link_full);
        evtName.setText(Events.get(position).getName());
        evtDesc.setText(Events.get(position).getDesc());
        Map<String,String> mp = Events.get(position).getCoordinator();
        ArrayList<String> names=new ArrayList<>();
        ArrayList<String> phones=new ArrayList<>();
        for (Map.Entry<String, String> entry : mp.entrySet()) {
            names.add(entry.getKey());
            phones.add(entry.getValue());
        }

        cord1Name.setText(names.get(0));
        cord2Name.setText(names.get(1));
        cord1Phone.setText(phones.get(0));
        cord2Phone.setText(phones.get(1));


        linkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Events.get(position).getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
