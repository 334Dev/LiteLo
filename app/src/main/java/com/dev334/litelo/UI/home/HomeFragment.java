package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment implements todayAdapter.ClickInterface{

    private static final String TAG ="HomeFragment" ;
    private HomeViewModel homeViewModel;
    private List<EventModel> Events;
    private RecyclerView todayRecycler;
    private todayAdapter AdapterToday;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances

        Events=new ArrayList<>();
        Events=((HomeActivity)getActivity()).getEvents();
        todayRecycler=root.findViewById(R.id.todayEventRecycler);
        setupTodayRecycler();

        return root;
    }

    private void setupTodayRecycler() {

        AdapterToday= new todayAdapter(Events,this);
        todayRecycler.setAdapter(AdapterToday);
        todayRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        todayRecycler.setHasFixedSize(true);
    }

    @Override
    public void recyclerviewOnClick(int position) {

    }
}
