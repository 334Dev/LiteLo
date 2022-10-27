package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.BranchActivity;
import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements todayAdapter.ClickInterface, branchAdapter.ClickInterface, filterAdapter.ClickInterface
, DatePickerDialog.OnDateSetListener
{

    private static final String TAG ="HomeFragment" ;
    private HomeViewModel homeViewModel;
    private List<EventModel> Events, filterEvents;
    private RecyclerView todayRecycler;
    private RecyclerView branchRecycler;

    private todayAdapter AdapterToday;
    private branchAdapter AdapterBranch;
    private filterAdapter AdapterFilter;
    private RecyclerView filterRecycler;
    private List<Map<String,Object>> branchList;
    private Spinner filterSpinner;
    private List<EventModel> fEvents;
    private List<Map<String, Object>> EventMap;
    private List<String> filter;
    private Integer FILTER=1;
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
        filterSpinner=root.findViewById(R.id.spinner2);
        fEvents=new ArrayList<>();
        EventMap=new ArrayList<>();
        filter=new ArrayList<>();

        filter.add("Tomorrow");
        filter.add("Today");
        filter.add("Pick a Date");

        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(), R.layout.dropdown_item_filter, filter);
        filterSpinner.setAdapter(arrayAdapter);


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

        if(Events.isEmpty()){
            root.findViewById(R.id.noevent_msg).setVisibility(View.VISIBLE);
        }


        filterEvents=((HomeActivity)getActivity()).getTomorrowEvents();

        todayRecycler=root.findViewById(R.id.todayEventRecycler);
        branchRecycler=root.findViewById(R.id.recyclerView2);
        filterRecycler=root.findViewById(R.id.filterEventRecycler);
        filterRecycler.setNestedScrollingEnabled(false);
        setupBranchRecycler();
        setupTodayRecycler();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    FILTER=1;
                    setupFilterTomorrowRecycler();
                }else if(i==1){
                    FILTER=0;
                    setupFilterTodayRecycler();
                }else if(i==2){
                    FILTER=2;
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
                    if(filter.size()==4) {
                        filter.remove(3);
                    }
                    DatePickerDialog datePicker = new DatePickerDialog(getContext(), datePickerListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.setCancelable(false);
                    datePicker.setTitle("Select the date");
                    datePicker.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                FILTER=1;
                setupFilterTomorrowRecycler();
            }
        });

        return root;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            selectedMonth=selectedMonth+1;
            String date=selectedYear+"-"+selectedMonth+"-"+selectedDay;
            filter.add(date);
            filterSpinner.setSelection(3);
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
            firestore.collection("Events").document(date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    fEvents.clear();
                    if(documentSnapshot.exists()){
                        EventMap= (List<Map<String, Object>>) documentSnapshot.get("Events");
                        EventMap.sort(new Comparator<Map<String, Object>>() {
                            @Override
                            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                                return m1.get("Time").toString().compareTo(m2.get("Time").toString());
                            }
                        });
                        fEvents=EventMap.stream().map(MapToEvents).collect(Collectors.<EventModel> toList());
                        setupFilterDateRecycler();
                    }
                    setupFilterDateRecycler();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e.getMessage());
                }
            });

        }
    };

    private void setupFilterDateRecycler() {
        AdapterFilter= new filterAdapter(fEvents,this,getContext());
        filterRecycler.setAdapter(AdapterFilter);
        filterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        filterRecycler.setHasFixedSize(true);
    }

    private void setupFilterTodayRecycler() {
        AdapterFilter= new filterAdapter(Events,this,getContext());
        filterRecycler.setAdapter(AdapterFilter);
        filterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        filterRecycler.setHasFixedSize(true);
    }

    private void setupFilterTomorrowRecycler() {
        AdapterFilter= new filterAdapter(filterEvents,this,getContext());
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
        View view=getLayoutInflater().inflate(R.layout.event_full_detail,null);
        alert.setView(view);
        AlertDialog show=alert.show();

        TextView evtName,evtDesc,cord1Name,cord2Name,cord1Phone,cord2Phone,linkMain,MeetingLink;
        evtName=view.findViewById(R.id.event_name_full);
        evtDesc=view.findViewById(R.id.event_desc_full);
        cord1Name=view.findViewById(R.id.coordinator_name_full);
        cord2Name=view.findViewById(R.id.coordinator_name_full2);
        cord1Phone=view.findViewById(R.id.coordinator_number_full);
        cord2Phone=view.findViewById(R.id.coordinator_number_full2);
        linkMain =view.findViewById(R.id.website_link_full);
        MeetingLink=view.findViewById(R.id.eCard_Link);

        Map<String, String> mp=new HashMap<>();
        evtName.setText(Events.get(position).getName());
        evtDesc.setText(Events.get(position).getDesc());
        mp = Events.get(position).getCoordinator();

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

        linkMain.setMovementMethod(LinkMovementMethod.getInstance());
        linkMain.setOnClickListener(v->{
            //on click
        });


        MeetingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(Events.get(position).getLink());

                if(uri.toString().isEmpty()){
                    Toast.makeText(getContext(), "No meeting scheduled yet", Toast.LENGTH_SHORT);
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
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

        TextView evtName,evtDesc,cord1Name,cord2Name,cord1Phone,cord2Phone,linkMain,MeetingLink;
        evtName=view.findViewById(R.id.event_name_full);
        evtDesc=view.findViewById(R.id.event_desc_full);
        cord1Name=view.findViewById(R.id.coordinator_name_full);
        cord2Name=view.findViewById(R.id.coordinator_name_full2);
        cord1Phone=view.findViewById(R.id.coordinator_number_full);
        cord2Phone=view.findViewById(R.id.coordinator_number_full2);
        linkMain =view.findViewById(R.id.website_link_full);
        MeetingLink=view.findViewById(R.id.eCard_Link);

        Map<String, String> mp=new HashMap<>();
        if(FILTER==0){
            evtName.setText(Events.get(position).getName());
            evtDesc.setText(Events.get(position).getDesc());
            mp = Events.get(position).getCoordinator();
        }
        else if(FILTER==1) {
            evtName.setText(filterEvents.get(position).getName());
            evtDesc.setText(filterEvents.get(position).getDesc());
            mp = filterEvents.get(position).getCoordinator();
        }else{
            evtName.setText(fEvents.get(position).getName());
            evtDesc.setText(fEvents.get(position).getDesc());
            mp = fEvents.get(position).getCoordinator();
        }

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

        linkMain.setMovementMethod(LinkMovementMethod.getInstance());
        linkMain.setOnClickListener(v->{
            //on click
        });


        MeetingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://avishkar.mnnit.ac.in/events/");
                if(FILTER==0){
                    uri=Uri.parse(Events.get(position).getLink());
                }else if(FILTER==1) {
                    uri=Uri.parse(filterEvents.get(position).getLink()); // missing 'http://' will cause crashed
                }else{
                    uri=Uri.parse(fEvents.get(position).getLink());
                }

                if(uri.toString().isEmpty()){
                    Toast.makeText(getContext(), "No meeting scheduled yet", Toast.LENGTH_SHORT);
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=month+1;
        Log.i("DateSelectedAdmin", "onDateSet: "+year+" "+month+" "+dayOfMonth);
        String date=year+"-"+month+"-"+dayOfMonth;
    }

    Function<Map<String, Object>, EventModel> MapToEvents = new Function<Map<String, Object>, EventModel>() {
        @Override
        public EventModel apply(Map<String, Object> stringObjectMap) {
            EventModel event = new EventModel(stringObjectMap);
            return event;
        }
    };

}
