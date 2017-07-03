package com.example.shushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.models.FriendModel;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.testfirebase.ChatActivity;
import com.example.shushi.testfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shushi on 6/8/2017.
 */


public class RecyclerViewFriendAdapter extends RecyclerView.Adapter<RecyclerViewFriendAdapter.MyViewHolder> {

    private ArrayList<ProfileModel> profilesList;


    public ArrayList<ProfileModel> getMoviesList() {
        return profilesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView title;
        public ImageView circleImageView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.txtdisplay_model_friend);
            circleImageView = (ImageView) view.findViewById(R.id.imageview_model_friend);

        }
    }

    public RecyclerViewFriendAdapter(ArrayList<ProfileModel> profilesList) {
        this.profilesList = profilesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.modelfriend, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProfileModel pro = profilesList.get(position);
        holder.title.setText(pro.getDisplayName());
        try {
            Picasso.with(holder.circleImageView.getContext()).load(pro.getUrlPhoto()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.circleImageView);
        } catch (Exception ex) {
            holder.circleImageView.setImageResource(R.drawable.giphy);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context va = v.getContext();
                Intent intent = new Intent(va, ChatActivity.class);
                intent.putExtra("keyPushFriend", pro.getEmailReset());
                intent.putExtra("urlImgFriend", pro.getUrlPhoto());
                va.startActivity(intent);
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context va = v.getContext();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return profilesList.size();
    }
}