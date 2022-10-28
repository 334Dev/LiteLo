package com.dev334.litelo.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.dev334.litelo.EventAdapter;
import com.dev334.litelo.R;
import com.dev334.litelo.model.DepartmentModel;
import com.dev334.litelo.model.EventModel;
import com.dev334.litelo.model.EventRequest;
import com.dev334.litelo.model.EventResponse;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchActivity extends AppCompatActivity {

    private TextView department, description;
    private RecyclerView eventsRecycler;
    private EventAdapter adapter;
    private DepartmentModel departmentModel;
    private List<EventModel> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        setReferences();
        getDepartment();
        if (departmentModel != null)
            fetchEvents();
    }

    private void setReferences() {
        department = findViewById(R.id.department);
        description = findViewById(R.id.description);
        eventsRecycler = findViewById(R.id.eventsRecycler);
    }

    private void getDepartment() {
        departmentModel = (DepartmentModel) getIntent().getSerializableExtra(Constants.DEPARTMENT);
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
                            setUpAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {

                    }
                });
    }

    private void setUpAdapter() {
        adapter = new EventAdapter(events, this);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventsRecycler.setAdapter(adapter);
    }
}