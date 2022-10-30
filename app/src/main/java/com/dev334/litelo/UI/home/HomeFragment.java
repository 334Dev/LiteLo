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

import com.dev334.litelo.UI.BranchActivity;
import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.model.DepartmentModel;
import com.dev334.litelo.model.DepartmentResponse;
import com.dev334.litelo.model.TimelineModel;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements BranchAdapter.ClickInterface, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private RecyclerView todayRecycler;
    private RecyclerView branchRecycler;
    private RecyclerView filterRecycler;
    private TodayEventAdapter AdapterToday;
    private BranchAdapter AdapterBranch;
    private FilterEventAdapter AdapterFilter;
    private List<DepartmentModel> departments;
    private Spinner filterSpinner;
    private List<String> filter;
    private Integer FILTER = 1;
    Map<String, Object> images = new HashMap<>();
    private List<Map<String, Object>> list = new ArrayList<>();
    private TextView noEvent;

    public HomeFragment() {
        //empty constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        departments = new ArrayList<>();
        filterSpinner = root.findViewById(R.id.spinner2);
        filter = new ArrayList<>();
        filter.add("Tomorrow");
        filter.add("Pick a Date");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item_filter, filter);
        filterSpinner.setAdapter(arrayAdapter);
        String[] branch_names = new String[]{
                "Cyberquest", "Oligopoly", "Techno Art", "Rasayans", "Kreedomania", "Monopoly", "Nirmaan", "Astrowing", "PowerSurge", "Mechrocosm", "Robomania",
                "Aerodynamix", "Genesis", "Electromania", "Gnosiomania"
        };
        Integer[] branch_logo = new Integer[]{
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
        for (int i = 0; i < 15; i++) {
            images.put(branch_names[i], branch_logo[i]);
        }
        noEvent = root.findViewById(R.id.noevent_msg);
        todayRecycler = root.findViewById(R.id.todayEventRecycler);
        branchRecycler = root.findViewById(R.id.recyclerView2);
        filterRecycler = root.findViewById(R.id.filterEventRecycler);

        todayRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        branchRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        filterRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        AdapterToday = new TodayEventAdapter(list, requireContext());
        AdapterFilter = new FilterEventAdapter(list, requireContext());

        filterRecycler.setNestedScrollingEnabled(false);

        todayRecycler.setAdapter(AdapterToday);
        filterRecycler.setAdapter(AdapterFilter);

        fetchDepartments();


        fetchEvents(getDate(0), true);
        fetchEvents(getDate(1), false);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    fetchEvents(getDate(1), false);
                else {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
                    if (filter.size() == 4) {
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void fetchEvents(String date, boolean today) {
        FirebaseFirestore.getInstance()
                .collection("DateWiseEvent")
                .document(date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            if (today) {
                                if (task.getResult().get("Events") != null) {
                                    AdapterToday.setList((List<Map<String, Object>>) task.getResult().get("Events"));
                                    AdapterToday.notifyDataSetChanged();
                                    noEvent.setVisibility(View.INVISIBLE);
                                } else {
                                    noEvent.setVisibility(View.VISIBLE);
                                }
                            } else if (task.getResult().get("Events") != null) {
                                AdapterFilter.setList((List<Map<String, Object>>) task.getResult().get("Events"));
                                AdapterFilter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    public String getDate(int tomorrow) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis() + tomorrow * (long) 24 * (long) 60 * (long) 60 * (long) 1000);
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }

    private void fetchDepartments() {
        RetrofitAccessObject.getRetrofitAccessObject().getDepartments().enqueue(new Callback<DepartmentResponse>() {
            @Override
            public void onResponse(Call<DepartmentResponse> call, Response<DepartmentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getDepartment() != null)
                        departments = response.body().getDepartment();
                    AdapterBranch = new BranchAdapter(departments, HomeFragment.this, images);
                    branchRecycler.setAdapter(AdapterBranch);
                }
            }

            @Override
            public void onFailure(Call<DepartmentResponse> call, Throwable t) {

            }
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            selectedMonth = selectedMonth + 1;
            String date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            filter.set(1, date);
            filterSpinner.setSelection(1);
            fetchEvents(date, false);
        }
    };

    @Override
    public void branchviewOnClick(int position) {
        Intent intent = new Intent(getActivity(), BranchActivity.class);
        intent.putExtra(Constants.DEPARTMENT, departments.get(position));
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        Log.i("DateSelectedAdmin", "onDateSet: " + year + " " + month + " " + dayOfMonth);
        String date = year + "-" + month + "-" + dayOfMonth;
    }

    Function<Map<String, Object>, TimelineModel> MapToEvents = new Function<Map<String, Object>, TimelineModel>() {
        @Override
        public TimelineModel apply(Map<String, Object> stringObjectMap) {
            TimelineModel event = new TimelineModel(stringObjectMap);
            return event;
        }
    };

}
