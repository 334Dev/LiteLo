package com.dev334.litelo.UI.splashScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev334.litelo.HomeActivity;
import com.dev334.litelo.Interfaces.PassDataInterface;
import com.dev334.litelo.Login.LoginActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.UI.home.EventModel;
import com.dev334.litelo.splashScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SplashFragment extends Fragment {

    PassDataInterface passDataInterface;
    public SplashFragment(PassDataInterface passDataInterface) {
        // Required empty public constructor
        this.passDataInterface=passDataInterface;
    }

    public SplashFragment() {
        // doesn't do anything special
    }

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String todayString;
    private String tomorrowString;
    private String TAG="SplashFragment";
    private List<EventModel> Events, TomorrowEvents;
    private List<Map<String, Object>> EventMap, tEventMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_splash, container, false);

        mAuth= FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        Calendar c=Calendar.getInstance();
        Date todayDate = c.getTime();
        c.add(Calendar.DAY_OF_YEAR,1);
        Date tomorrowDate = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        todayString = formatter.format(todayDate);
        tomorrowString=formatter.format(tomorrowDate);

        Log.i(TAG, "onCreateView: "+todayString+" "+tomorrowString);

        EventMap=new ArrayList<>();
        Events=new ArrayList<>();
        TomorrowEvents=new ArrayList<>();

        //handler to delay
        Handler mHandler= new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser()==null){
                    ((HomeActivity)getActivity()).openLoginActivity(0);
                }
                else {

                    String UserID=mAuth.getCurrentUser().getUid();
                    FirebaseUser user=mAuth.getCurrentUser();

                    if(!user.getPhoneNumber().isEmpty()){
                        firestore.collection("NewUsers").document(UserID).
                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FirebaseMessaging.getInstance().subscribeToTopic("Avishkar");
                                    fetchDataToday();
                                } else {
                                    ((HomeActivity)getActivity()).openLoginActivity(2);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Check Details", "onFailure: " + e.getMessage());
                            }
                        });
                    }

                    else if(!user.isEmailVerified()){
                        ((HomeActivity)getActivity()).openLoginActivity(1);
                    }
                    else {
                        firestore.collection("NewUsers").document(UserID).
                                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    FirebaseMessaging.getInstance().subscribeToTopic("Avishkar");
                                    fetchDataToday();

                                } else {
                                    ((HomeActivity)getActivity()).openLoginActivity(2);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Check Details", "onFailure: " + e.getMessage());
                            }
                        });
                    }
                }
            }
        },500);



        return root;
    }

    private void fetchDataToday() {
        firestore.collection("Events").document(todayString)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    EventMap = (List<Map<String, Object>>) documentSnapshot.get("Events");
                    Events = EventMap.stream().map(MapToEvents).collect(Collectors.<EventModel>toList());
                    Log.i(TAG, "onSuccess: " + Events.get(0).getName());
                }
                passDataInterface.PassTodayEvents(Events);
                fetchDataTomorrow();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void fetchDataTomorrow() {
        String test = "2021-12-19";
        firestore.collection("Events").document(tomorrowString)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    tEventMap = (List<Map<String, Object>>) documentSnapshot.get("Events");
                    TomorrowEvents = tEventMap.stream().map(MapToEvents).collect(Collectors.<EventModel>toList());
                    Log.i(TAG, "onSuccess: " + TomorrowEvents.get(0).getName());
                }
                passDataInterface.PassTomorrowEvents(TomorrowEvents);
                ((HomeActivity)getActivity()).setHomeFragment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    Function<Map<String, Object>, EventModel> MapToEvents = new Function<Map<String, Object>, EventModel>() {
        @Override
        public EventModel apply(Map<String, Object> stringObjectMap) {
            EventModel event = new EventModel(stringObjectMap);
            return event;
        }
    };

}