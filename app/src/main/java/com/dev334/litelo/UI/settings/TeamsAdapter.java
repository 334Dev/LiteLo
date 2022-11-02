package com.dev334.litelo.UI.settings;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.Participation;
import com.dev334.litelo.model.Team;
import com.dev334.litelo.model.member.Member;
import com.dev334.litelo.model.member.TeamMemberResponse;
import com.dev334.litelo.utility.Constants;
import com.dev334.litelo.utility.RetrofitAccessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamsAdapter extends RecyclerView.Adapter<com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH> {
    private List<Team> teams;
    private Map<String, TeamMemberAdapter> adapterMap = new HashMap<>();
    private Map<String, List<Member>> memberMap = new HashMap<>();
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
        private TextView name, status_indicator, events;
        private RecyclerView recyclerView;
        private ImageView img;
        private TeamMemberAdapter adapter;
        private CardView statusDot;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.team_name);
            status_indicator = itemView.findViewById(R.id.status);
            recyclerView = itemView.findViewById(R.id.teams_recycler_view);
            events = itemView.findViewById(R.id.events);
            img = itemView.findViewById(R.id.expansion);
            statusDot = itemView.findViewById(R.id.statusDot);
            recyclerView = itemView.findViewById(R.id.teams_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapter = new TeamMemberAdapter(new ArrayList<>(), context);
            recyclerView.setAdapter(adapter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerView.getVisibility() == View.GONE) {
                        img.setImageResource(R.drawable.ic_baseline_expand_less_24);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        img.setImageResource(R.drawable.ic_baseline_expand_circle_down_24);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }

        public void setView(Team team) {
            name.setText(team.getTeam().getName());
            status_indicator.setText(team.getStatus());
            if (team.getStatus() == "PENDING") {
                statusDot.setCardBackgroundColor(ContextCompat.getColor(statusDot.getContext(), R.color.red));
            } else {
                statusDot.setCardBackgroundColor(ContextCompat.getColor(statusDot.getContext(), R.color.green));
            }
            String str = "";
            for (Participation p : team.getTeam().getParticipation()) {
                str += p.getEvent().getName() + ", ";
            }
            if (str.equals(""))
                str = "No events";
            else
                str = str.substring(0, str.length() - 2);

            events.setText(str);

            if (!memberMap.containsKey(String.valueOf(team.getTeamId())))
                getMembers(team.getTeamId());
            else {
                adapter.setMembers(memberMap.get(String.valueOf(team.getTeamId())));
                adapter.notifyDataSetChanged();
            }

            if (recyclerView.getVisibility() == View.GONE) {
                img.setImageResource(R.drawable.ic_baseline_expand_circle_down_24);
            } else {
                img.setImageResource(R.drawable.ic_baseline_expand_less_24);
            }
        }

        public void getMembers(Integer id) {
            RetrofitAccessObject.getRetrofitAccessObject()
                    .getTeamMembers(id, context.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE).getString(Constants.TOKEN, ""))
                    .enqueue(new Callback<TeamMemberResponse>() {
                        @Override
                        public void onResponse(Call<TeamMemberResponse> call, Response<TeamMemberResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
                                List<Member> toRemove = new ArrayList<>();
                                for (Member member : response.body().getMembers()) {
                                    if (member.getUser().getName().equals(""))
                                        toRemove.add(member);
                                }
                                response.body().getMembers().removeAll(toRemove);
                                memberMap.put(String.valueOf(id), response.body().getMembers());
                                adapter.setMembers(memberMap.get(String.valueOf(id)));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<TeamMemberResponse> call, Throwable t) {

                        }
                    });
        }
    }

    private String appendZero(int i) {
        if (i > 9)
            return String.valueOf(i);
        return "0" + i;
    }

}
