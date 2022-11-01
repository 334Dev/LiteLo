package com.dev334.litelo.UI.settings;


import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dev334.litelo.R;
import com.dev334.litelo.model.Participation;
import com.dev334.litelo.model.Team;
import com.dev334.litelo.model.member.Member;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TeamsAdapter extends RecyclerView.Adapter<com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH> {
private List<Member> members;
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
        private ImageView img;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.team_name);
            status_indicator = itemView.findViewById(R.id.status);
            recyclerView = itemView.findViewById(R.id.teams_recycler_view);
            events = itemView.findViewById(R.id.events);
            img = itemView.findViewById(R.id.expansion);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerView.getVisibility() == View.GONE){
                        recyclerView.setVisibility(View.VISIBLE);
                    }else{
                        recyclerView.setVisibility(View.GONE);
                    }
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
 /*          getMembers(team.getTeam().getId());

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.team_details_dialog, null);

            RecyclerView recyclerView = view.findViewById(R.id.team_members);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            TeamMemberAdapter adapter = new TeamMemberAdapter(members , context);
            recyclerView.setAdapter(adapter);

       */

            if(recyclerView.getVisibility() == View.GONE){
                img.setImageResource(R.drawable.ic_baseline_expand_circle_down_24);
            }else{
                img.setImageResource(R.drawable.ic_baseline_expand_less_24);
            }
        }
    }

    private String appendZero(int i) {
        if (i > 9)
            return String.valueOf(i);
        return "0" + i;
    }

    public void getMembers(String id){

    }

}
