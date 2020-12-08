package com.example.litelo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResourcesFragment extends Fragment implements resourceAdapter.onNoteListener {
    private View view;
    private RecyclerView resourceRecycler;
    private String[] resourceModels={"Mathematics","Physics","Chemistry","Engineering Mechanics","Workshop","Computer Science"};
    private resourceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_resources, container, false);
        resourceRecycler=view.findViewById(R.id.resourceRecycler);

        adapter= new resourceAdapter(resourceModels,this);
        resourceRecycler.setAdapter(adapter);
        resourceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        resourceRecycler.setHasFixedSize(true);


        return view;
    }

    @Override
    public void onNoteClick(int position) {
        Intent i=new Intent(getActivity(),subjectResources.class);
        i.putExtra("Subject",resourceModels[position]);
        startActivity(i);
    }
}