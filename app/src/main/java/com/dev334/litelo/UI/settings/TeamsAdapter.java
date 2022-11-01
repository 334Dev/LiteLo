package com.dev334.litelo.UI.settings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.AdminActivity;
import com.dev334.litelo.EventActivity;
import com.dev334.litelo.R;
import com.dev334.litelo.model.NotificationModel;
import com.dev334.litelo.model.Participation;
import com.dev334.litelo.model.Team;
import com.dev334.litelo.model.Team__1;
import com.dev334.litelo.utility.Constants;
import com.google.type.ColorOrBuilder;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TeamsAdapter extends RecyclerView.Adapter<com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH> {

    private List<Team> teams;
    Context context;

    public TeamsAdapter(List<Team> teams, Context context) {
        this.teams = teams;
        this.context = context;
    }

    @NonNull
    @Override
    public com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH(View.inflate(parent.getContext(), R.layout.team_details_cardview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH holder, int position) {
        holder.setView(teams.get(position));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        private TextView name, status_indicator,events;
        private RecyclerView recyclerView;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.team_name);
            status_indicator = itemView.findViewById(R.id.status);
            recyclerView = itemView.findViewById(R.id.teams_recycler_view);
            events = itemView.findViewById(R.id.events);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        public void setView(Team team) {
            name.setText(team.getTeam().getName());
            status_indicator.setText(team.getStatus());
            if(team.getStatus() == "PENDING"){
                    status_indicator.setTextColor(Color.RED);
            }else{
                status_indicator.setTextColor(Color.GREEN);
            }

            String str = "";
            for(Participation p : team.getTeam().getParticipation()){
                str +=  p.getEvent().getName() + ", ";
            }
            events.setText(str);
        }

        getMembers();

    }

    private String format(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        if (c.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
            return c.get(Calendar.DAY_OF_MONTH) +
                    " " +
                    c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                    " at " +
                    c.get(Calendar.HOUR) +
                    ":" +
                    appendZero(c.get(Calendar.MINUTE)) +
                    " " +
                    c.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.getDefault());
        return c.get(Calendar.DAY_OF_MONTH) +
                " " +
                c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) +
                " " +
                c.get(Calendar.YEAR) +
                " at " +
                c.get(Calendar.HOUR) +
                ":" +
                appendZero(c.get(Calendar.MINUTE)) +
                " " +
                c.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.getDefault());
    }

    public void getMembers(){

    }

    private String appendZero(int i) {
        if (i > 9)
            return String.valueOf(i);
        return "0" + i;
    }
//
//    public void showTeamMembers(Team team){
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(context);
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.team_details_dialog, null);
//        TextView team_name,events;
//        ListView team_members;
//
//        ImageView closeAlert = view.findViewById(R.id.addEvent_close);
//        team_name = view.findViewById(R.id.team_name);
//        events = view.findViewById(R.id.events_tv);
//        team_members = view.findViewById(R.id.team_members);
//
//        alert.setView(view);
//        AlertDialog show = alert.show();
//
//        closeAlert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                show.dismiss();
//            }
//        });
//
////
////
////        events.setText(str);
////        team_name.setText(team.getTeam().getName());
//
//    }

}
