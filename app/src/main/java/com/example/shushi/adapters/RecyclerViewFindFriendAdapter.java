package com.example.shushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.controller.MeThodMessage;
import com.example.shushi.models.MessageChat;
import com.example.shushi.models.ProfileInviteModel;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.testfirebase.ChatActivity;
import com.example.shushi.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.shushi.controller.MeThodMessage.addFriend;
import static com.example.shushi.controller.MeThodMessage.addFriendWithInvite;
import static com.example.shushi.controller.MeThodMessage.deleteFriend;
import static com.example.shushi.controller.MeThodMessage.deleteInvite;
import static com.example.shushi.controller.MeThodMessage.sendInvite;
import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;

/**
 * Created by Shushi on 6/9/2017.
 */

public class RecyclerViewFindFriendAdapter extends RecyclerView.Adapter<RecyclerViewFindFriendAdapter.MyViewHolder> {
    private ArrayList<ProfileInviteModel> profileModels;


    public ArrayList<ProfileInviteModel> getProfileModels() {
        return profileModels;
    }

    public RecyclerViewFindFriendAdapter(ArrayList<ProfileInviteModel> profilesList) {
        this.profileModels = profilesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.modelfindfriend, parent, false);

        return new RecyclerViewFindFriendAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProfileInviteModel profile = profileModels.get(position);
        holder.title.setText(profile.getDisplayName());
        try {
            Picasso.with(holder.circleImageView.getContext()).load(profile.getUrlPhoto()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.circleImageView);
        } catch (Exception ex) {
            holder.circleImageView.setImageResource(R.drawable.giphy);
        }
        if (profile.isFriend()) {
            holder.btnaddFriend.setText("Kết Bạn");
            holder.btnremoveAddfriend.setText("Xóa Bạn");
            holder.btnaddFriend.setEnabled(false);
            holder.btnremoveAddfriend.setEnabled(true);
            holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
            holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));

        } else {

            if (profile.isInvited()) {
                holder.btnaddFriend.setText("Kết Bạn");
                holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                holder.btnaddFriend.setEnabled(false);
                holder.btnremoveAddfriend.setEnabled(true);
                holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));

            } else {
                if (profile.isBeInvited()) {
                    holder.btnaddFriend.setText("Chấp Nhận");
                    holder.btnremoveAddfriend.setText("Từ Chối");
                    holder.btnaddFriend.setEnabled(true);
                    holder.btnremoveAddfriend.setEnabled(true);
                    holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                    holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));

                } else {
                    holder.btnaddFriend.setText("Kết Bạn");
                    holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                    holder.btnaddFriend.setEnabled(true);
                    holder.btnremoveAddfriend.setEnabled(false);
                    holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                    holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));

                }
            }
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference();
        holder.btnremoveAddfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.btnremoveAddfriend.getText().toString()) {
                    case "Xóa Bạn":
                        deleteFriend(EncodeString(user.getEmail()), EncodeString(addSuffixeString(profile.getEmailReset())), root);
                        holder.btnaddFriend.setText("Kết Bạn");
                        holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                        holder.btnaddFriend.setEnabled(true);
                        holder.btnremoveAddfriend.setEnabled(false);
                        holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                        holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                        break;
                    case "Hủy Kết Bạn":
                        deleteInvite(EncodeString(user.getEmail()), EncodeString(addSuffixeString(profile.getEmailReset())), root);
                        holder.btnaddFriend.setText("Kết Bạn");
                        holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                        holder.btnaddFriend.setEnabled(true);
                        holder.btnremoveAddfriend.setEnabled(false);
                        holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                        holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                        break;
                    case "Từ Chối":
                        deleteInvite(EncodeString(addSuffixeString(profile.getEmailReset())), EncodeString(user.getEmail()), root);
                        holder.btnaddFriend.setText("Kết Bạn");
                        holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                        holder.btnaddFriend.setEnabled(true);
                        holder.btnremoveAddfriend.setEnabled(false);
                        holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                        holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                        break;
                }
            }
        });
        holder.btnaddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.btnaddFriend.getText().toString()) {
                    case "Chấp Nhận":
                        MessageChat ms = new MessageChat();
                        ms.setTextMessage("Hai người đã là bạn");
                        Calendar today = Calendar.getInstance();
                        ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                        addFriendWithInvite(EncodeString(user.getEmail()), EncodeString(addSuffixeString(profile.getEmailReset())), root, ms, true);
                        holder.btnaddFriend.setText("Kết Bạn");
                        holder.btnremoveAddfriend.setText("Xóa Bạn");
                        holder.btnaddFriend.setEnabled(false);
                        holder.btnremoveAddfriend.setEnabled(true);
                        holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                        holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                        break;
                    case "Kết Bạn":
                        sendInvite(EncodeString(user.getEmail()), EncodeString(addSuffixeString(profile.getEmailReset())), root);
                        holder.btnaddFriend.setText("Kết Bạn");
                        holder.btnremoveAddfriend.setText("Hủy Kết Bạn");
                        holder.btnaddFriend.setEnabled(false);
                        holder.btnremoveAddfriend.setEnabled(true);
                        holder.btnaddFriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_not_enable));
                        holder.btnremoveAddfriend.setBackground(holder.mView.getResources().getDrawable(R.drawable.button_selector));
                        break;
                }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile.isFriend()) {
                    Context va = v.getContext();
                    Intent intent = new Intent(va, ChatActivity.class);
                    intent.putExtra("keyPushFriend", profile.getEmailReset());
                    va.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return profileModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView title;
        public ImageView circleImageView;
        public Button btnaddFriend, btnremoveAddfriend;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.txtdisplay_model_friend);
            circleImageView = (ImageView) view.findViewById(R.id.imageview_model_friend);
            btnaddFriend = (Button) view.findViewById(R.id.addFriend);
            btnremoveAddfriend = (Button) view.findViewById(R.id.removeAddFriend);
        }
    }


}
