package com.dev334.litelo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrowseClubFragment extends Fragment implements BrowseClubAdapter.onNoteListener{

    private View root;
    private ViewPager2 viewPager;
    private FirebaseFirestore firestore;
    private List<String> link,logo,description,club,type,mClub, mLogo;
    private static int FILTER=1,STATUS=0;
    private BrowseClubAdapter browseClubAdapter;
    private TextView BigClubName, allText, culturalText, technicalText, welfareText;
    private LinearLayout all,cultural,technical,welfare;
    private FirebaseAuth mAuth;
    private List<String> subscribedClubs;
    private Button subscribe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_browse_club, container, false);

        viewPager=root.findViewById(R.id.bowseClubViewPager);
        BigClubName=root.findViewById(R.id.trans_Club);

        allText=root.findViewById(R.id.all_text);
        culturalText=root.findViewById(R.id.cultural_text);
        technicalText=root.findViewById(R.id.technical_text);
        welfareText=root.findViewById(R.id.welfare_text);

        all=root.findViewById(R.id.all);
        cultural=root.findViewById(R.id.cultural);
        technical=root.findViewById(R.id.technical);
        welfare=root.findViewById(R.id.welfare);

        link=new ArrayList<>();
        logo=new ArrayList<>();
        description=new ArrayList<>();
        club=new ArrayList<>();
        type=new ArrayList<>();

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FILTER!=1) {
                    FILTER = 1;
                    removePages();
                    allText.setVisibility(View.VISIBLE);
                    culturalText.setVisibility(View.INVISIBLE);
                    technicalText.setVisibility(View.INVISIBLE);
                    welfareText.setVisibility(View.INVISIBLE);

                }
            }
        });

        cultural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FILTER!=2) {
                    FILTER = 2;
                    removePages();
                    allText.setVisibility(View.INVISIBLE);
                    culturalText.setVisibility(View.VISIBLE);
                    technicalText.setVisibility(View.INVISIBLE);
                    welfareText.setVisibility(View.INVISIBLE);

                }
            }
        });

        technical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FILTER!=3) {
                    FILTER = 3;
                    removePages();
                    allText.setVisibility(View.INVISIBLE);
                    culturalText.setVisibility(View.INVISIBLE);
                    technicalText.setVisibility(View.VISIBLE);
                    welfareText.setVisibility(View.INVISIBLE);

                }
            }
        });

        welfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FILTER!=4) {
                    FILTER = 4;
                    removePages();
                    allText.setVisibility(View.INVISIBLE);
                    culturalText.setVisibility(View.INVISIBLE);
                    technicalText.setVisibility(View.INVISIBLE);
                    welfareText.setVisibility(View.VISIBLE);

                }
            }
        });

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        firestore.collection("ClubClasses").document("CC").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    link= (List<String>) value.get("links");
                    logo= (List<String>) value.get("logo");
                    description= (List<String>) value.get("description");
                    club= (List<String>) value.get("clubs");
                    type= (List<String>) value.get("type");
                }

                if(club.isEmpty()){
                    Log.i("BrowseClubs", "onEvent: Empty");
                }else{

                    setupViewPager(logo, club);

                }
            }
        });

        userSubscribed();

        return root;
    }

    private void setupViewPager(List<String> mLogo, List<String> mClub ) {
        browseClubAdapter = new BrowseClubAdapter(mLogo,mClub,this);
        viewPager.setAdapter(browseClubAdapter);
        viewPager.setPadding(0,0,250,0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(4);
        //OnPageSelected
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(final int position) {
                BigClubName.setText(club.get(position));
            }
        });

    }

    private void removePages() {

        if(FILTER==1){
            setupViewPager(logo,club);
            return;
        }

        mClub=new ArrayList<>();
        mLogo=new ArrayList<>();

        for(int i=0;i<type.size();i++){
            if ("cultural".equals(type.get(i))) {
                if(FILTER==2){
                    mClub.add(club.get(i));
                    mLogo.add(logo.get(i));
                }
            } else if ("technical".equals(type.get(i))) {
                if(FILTER==3){
                    mClub.add(club.get(i));
                    mLogo.add(logo.get(i));
                }
            } else if ("welfare".equals(type.get(i))) {
                if(FILTER==4){
                    mClub.add(club.get(i));
                    mLogo.add(logo.get(i));
                }
            }
        }

        setupViewPager(mLogo, mClub);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onNoteClick(final int position) {
        final AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.club_dialog,null);

        int pos=position;
        if(FILTER!=1) {
            String clubname=mClub.get(position);
            for (int i = 0; i < club.size(); i++) {
                if (clubname.equals(club.get(i))) {
                    pos = i;
                }
            }
        }

        Button facebook=view.findViewById(R.id.facbook_dialog);
        subscribe=view.findViewById(R.id.subscribe_dialog);
        ImageView clubLogoD=view.findViewById(R.id.logo_dialog);
        TextView clubNameD=view.findViewById(R.id.club_dialog);
        TextView clubDescD=view.findViewById(R.id.desc_dialog);
        TextView clubTypeD=view.findViewById(R.id.type_dialog);

        if(link.get(pos).equals("NA")){
            facebook.setVisibility(View.GONE);
        }
        if(logo.get(pos).equals("NA")){
            Log.i("clubLogo", "onNoteClick: No Logo");
            clubLogoD.setImageResource(R.drawable.ic_baseline_explore_24);
        }else{
            Picasso.get()
                    .load(logo.get(pos))
                    .into(clubLogoD);
        }


        clubNameD.setText(club.get(pos));
        clubDescD.setText(description.get(pos));
        clubTypeD.setText("Type:\n"+type.get(pos));

        if(subscribedClubs.isEmpty()){
            STATUS=0;
        }else{
            if(subscribedClubsContains(club.get(pos))){
                STATUS=1;
                subscribe.setText("Subscribed");
                subscribe.setBackground(getResources().getDrawable(R.drawable.black_round_button));

            }else{
                STATUS=0;
            }
        }


        final int finalPos = pos;
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic=club.get(finalPos);
                if(STATUS==0) {
                    addtoFirestore(topic);
                    STATUS=1;
                }else{
                    STATUS=0;
                    removeFromFirestore(topic);
                }
            }
        });

        alert.setView(view);
        final int finalPos2 = pos;
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FBlink=link.get(finalPos2);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(FBlink));
                startActivity(i);
            }
        });

        alert.setCancelable(true);
        AlertDialog show=alert.show();
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void removeFromFirestore(final String topic) {
        firestore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .update("subscribedClubs", FieldValue.arrayRemove(topic)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                subscribe.setText("Subscribe");
                subscribe.setBackground(getResources().getDrawable(R.drawable.greenbutton));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("updateSubscribedClubs", "onFailure: "+e.getMessage());
            }
        });
    }

    private void addtoFirestore(final String topic) {
        firestore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .update("subscribedClubs", FieldValue.arrayUnion(topic)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                subscribe.setText("Subscribed");
                subscribe.setBackground(getResources().getDrawable(R.drawable.black_round_button));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("updateSubscribedClubs", "onFailure: "+e.getMessage());
            }
        });
    }

    private boolean subscribedClubsContains(String s) {
        for(int i=0;i<subscribedClubs.size();i++){
            if(subscribedClubs.get(i).equals(s)){
                return true;
            }
        }
        return false;
    }

    private void userSubscribed() {
        subscribedClubs=new ArrayList<>();
        firestore.collection("Users").document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.get("subscribedClubs")!=null){
                    subscribedClubs= (List<String>) value.get("subscribedClubs");
                }else{
                    Log.i("subscribedClubs", "onEvent: Field Not Present");
                }
            }
        });
    }
}