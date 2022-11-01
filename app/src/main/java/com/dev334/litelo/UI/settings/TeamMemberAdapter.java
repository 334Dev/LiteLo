package com.dev334.litelo.UI.settings;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dev334.litelo.R;
import com.dev334.litelo.model.member.Member;
import java.util.List;

public class TeamMemberAdapter extends RecyclerView.Adapter<com.dev334.litelo.UI.settings.TeamMemberAdapter.CustomVH> {
    private List<Member> members;
    Context context;

    public TeamMemberAdapter(List<Member>  members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public com.dev334.litelo.UI.settings.TeamMemberAdapter.CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.dev334.litelo.UI.settings.TeamMemberAdapter.CustomVH(View.inflate(parent.getContext(), R.layout.team_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull com.dev334.litelo.UI.settings.TeamMemberAdapter.CustomVH holder, int position) {
        holder.setView(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {
        private TextView name;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.member_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        public void setView(Member member) {
            name.setText(member.getUser().getName());
        }

    }
}
