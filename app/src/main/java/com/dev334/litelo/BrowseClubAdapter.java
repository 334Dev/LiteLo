package com.dev334.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BrowseClubAdapter extends RecyclerView.Adapter<BrowseClubAdapter.mViewHolder> {


    private List<String> link,club;
    private onNoteListener mOnNoteListener;

    public BrowseClubAdapter(List<String> link, List<String> club, onNoteListener onNoteListener) {

        this.club=club;
        this.link=link;
        this.mOnNoteListener=onNoteListener;

    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browseclub_model, parent, false);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new BrowseClubAdapter.mViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {

            holder.setImageView(link.get(position));
            holder.setClubText(club.get(position));

    }

    @Override
    public int getItemCount() {
        return club.size();
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        ImageView LogoImg;
        TextView clubName;
        onNoteListener onNotelistener;

        public mViewHolder(@NonNull View itemView, onNoteListener onNotelistener) {
            super(itemView);
            view=itemView;
            itemView.setOnClickListener(this);
            this.onNotelistener=onNotelistener;
            LogoImg=view.findViewById(R.id.vp_club_logo);
            clubName=view.findViewById(R.id.vp_club_name);
        }

        public void setImageView(String Link){
            Picasso.get()
                    .load(Link)
                    .into(LogoImg);
        }

        public void setClubText(String s) {
            clubName.setText(s);
        }

        @Override
        public void onClick(View v) {
            onNotelistener.onNoteClick(getAdapterPosition());
        }
    }


}
