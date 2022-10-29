package com.dev334.litelo.UI.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev334.litelo.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class NotificationFragment extends Fragment {

    private View view;
    private RecyclerView notificationRecyclerView;
    private FirebaseFirestore firestore;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        fetchNotifications();
        notificationRecyclerView=view.findViewById(R.id.notifaction_recycler);
        notificationRecyclerView.setAdapter(notificationAdapter);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerView.setHasFixedSize(true);

//        firestore=FirebaseFirestore.getInstance();

        return view;
    }

    private void fetchNotifications() {


    }

}