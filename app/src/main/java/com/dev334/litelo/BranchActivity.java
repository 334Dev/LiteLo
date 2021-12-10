package com.dev334.litelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.UI.home.eventAdapter;
import com.dev334.litelo.UI.home.filterAdapter;
import com.dev334.litelo.UI.home.todayAdapter;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

    }
}