package com.example.litelo.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.litelo.R;

public class SubjectList extends Fragment {

    private subjectListModel subjectListModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subjectListModel =
                ViewModelProviders.of(this).get(subjectListModel.class);
        View root = inflater.inflate(R.layout.fragment_subjectlist, container, false);

        return root;
    }
}