package com.dev334.litelo.UI.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.NotificationModel;
import com.dev334.litelo.utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private View view;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationsToShow = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_second, container, false);

        notificationRecyclerView = view.findViewById(R.id.notificationRecycler);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notificationRecyclerView.setNestedScrollingEnabled(false);
        fetchNotifications();
        return view;
    }

    private void fetchDummyNotification() {

    }

    private void fetchNotifications() {
        FirebaseFirestore.getInstance()
                .collection("Notifications")
                .document("Notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences preferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
                            List<Map<String, Object>> notifications = new ArrayList<>();
                            if (task.getResult() != null && task.getResult().get("Notifications") != null)
                                notifications.addAll((List<Map<String, Object>>) task.getResult().get("Notifications"));
                            for (Map<String, Object> mp : notifications) {
                                if (preferences.getBoolean((String) mp.get("eventId"), false))
                                    notificationsToShow.add(new NotificationModel((String) mp.get("eventId"), (String) mp.get("title"), (String) mp.get("body"), (long) mp.get("time")));
                            }
                            notificationsToShow.sort(new Comparator<NotificationModel>() {
                                @Override
                                public int compare(NotificationModel o1, NotificationModel o2) {
                                    if (o1.getTime() > o2.getTime()) return -1;
                                    else if (o1.getTime() < o2.getTime()) return 1;
                                    return 0;
                                }
                            });
                            notificationAdapter = new NotificationAdapter(notificationsToShow, requireContext());
                            notificationRecyclerView.setAdapter(notificationAdapter);
                        }
                    }
                });
    }
}