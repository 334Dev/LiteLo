package com.dev334.litelo.UI.home;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.viewpager2.widget.ViewPager2;

import com.dev334.litelo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class HomeFragment extends Fragment {

    private static final String TAG ="OfflineFirestore" ;
    private static final String TAG2 ="DayDebug" ;
    private static final String TAG3 ="NewDatabase" ;
    private AlertDialog.Builder builder;
    private AlertDialog show;
    private HomeViewModel homeViewModel;
    private FirebaseFirestore firestore;
    private String group;
    private String UserID;
    private FirebaseAuth mAuth;
    private List<String> todayClass;
    private List<Double> timing;
    private ViewPager2 viewPager;
    private Button presentAll,classesAll;
    private Map<String, Object> mMap;
    private String[] groupsMech, groupsChem;
    //SlideUp
    private CircularSeekBar SlideSeekBar;
    private TextView TotalAttend, TotalNotAttend, Remaining, SlidePercentage;
    private ImageView Attendplus, NotAttendplus, Attendminus, NotAttendminus;

    private SharedPreferences sharedPref;
    private LinearLayout linear1, linear2;

    //newVariables
    private List<String> todaySubject;
    private List<Long> subjectTiming;
    private Map<String, Long> ClassMap;
    //private List<Number>

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Firebase Instances

        return root;
    }



}
