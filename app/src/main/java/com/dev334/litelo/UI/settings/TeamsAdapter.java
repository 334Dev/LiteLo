package com.dev334.litelo.UI.settings;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.litelo.R;
import com.dev334.litelo.model.NotificationModel;
import com.dev334.litelo.model.TeamsModel;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TeamsAdapter extends RecyclerView.Adapter<com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH> {

    private List<TeamsModel> teams;
    Context context;

    public TeamsAdapter(List<TeamsModel> teams, Context context) {
        this.teams = teams;
        this.context = context;
    }

    @NonNull
    @Override
    public com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new com.dev334.litelo.UI.settings.TeamsAdapter.CustomVH(View.inflate(parent.getContext(), R.layout.notification_list_item, null));
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
        private TextView name, time;
        private ExpandableTextView desc;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.notification_desc_tv).findViewById(R.id.notification_desc_tv);
            time = itemView.findViewById(R.id.date_time);
        }

        public void setView(TeamsModel teamsModel) {
            name.setText(teamsModel.getTitle());
        }
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

    private String appendZero(int i) {
        if (i > 9)
            return String.valueOf(i);
        return "0" + i;
    }
}
