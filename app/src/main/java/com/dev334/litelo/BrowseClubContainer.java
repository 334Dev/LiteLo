package com.dev334.litelo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BrowseClubContainer extends AppCompatActivity {

    BrowseClubFragment browseClubFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_club_container);

        browseClubFragment=new BrowseClubFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.club_container,browseClubFragment).commit();

    }
}