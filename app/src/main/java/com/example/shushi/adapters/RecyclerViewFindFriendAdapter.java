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

import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;

/**
 * Created by Shushi on 6/9/2017.
 */

public class RecyclerViewFindFriendAdapter  extends RecyclerView.Adapter<RecyclerViewFindFriendAdapter.MyViewHolder>{
    private ArrayList<ProfileInviteModel> profileModels;


    public ArrayList<ProfileInviteModel> getProfileModels (){
        return profileModels;
    }

    public RecyclerViewFindFriendAdapter(ArrayList<ProfileInviteModel> moviesList) {
        this.profileModels = moviesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.modelfindfriend, parent, false);

        return new RecyclerViewFindFriendAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProfileInviteModel movie = profileModels.get(position);
        holder.title.setText(movie.getDisplayName());
        try {
            Picasso.with(holder.circleImageView.getContext()).load(movie.getUrlPhoto()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.circleImageView);
        } catch (Exception ex) {
            holder.circleImageView.setImageResource(R.drawable.giphy);
        }
        if(movie.isFriend()){
            holder.btnaddFriend.setVisibility(View.INVISIBLE);
        }else {
            holder.btnaddFriend.setVisibility(View.VISIBLE);
            if (movie.isInvited()) {
                holder.btnaddFriend.setText("Hủy Kết Bạn");
            }
            else {
                holder.btnaddFriend.setText("Kết Bạn");
            }
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference root;
        root = FirebaseDatabase.getInstance().getReference();
        holder.btnaddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (holder.btnaddFriend.getText().equals("Hủy Kết Bạn")) {
                        deleteInvite(EncodeString(user.getEmail()), EncodeString(addSuffixeString(movie.getEmailReset())), root);
                        holder.btnaddFriend.setText("Kết Bạn");
                    } else {
                        sendInvite(EncodeString(user.getEmail()), EncodeString(addSuffixeString(movie.getEmailReset())), root);
                        holder.btnaddFriend.setText("Hủy Kết Bạn");
                    }
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movie.isFriend()) {
                    Context va = v.getContext();
                    Intent intent = new Intent(va, ChatActivity.class);
                    intent.putExtra("keyPushFriend", movie.getEmailReset());

                    va.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return profileModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder   {
        public final View mView;
        public TextView title;
        public ImageView circleImageView;
        public Button btnaddFriend;
        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.txtdisplay_model_friend);
            circleImageView = (ImageView) view.findViewById(R.id.imageview_model_friend);
            btnaddFriend=(Button)view.findViewById(R.id.addFriend);
        }
    }

    private void sendInvite(String send,String receive,DatabaseReference databaseReference){
        databaseReference.child("Users").child(send).child("Invite").child(receive).child("keypushFriend").setValue(receive);
        databaseReference.child("Users").child(receive).child("Invited").child(send).child("keypushFriend").setValue(send);
    }
    private void deleteInvite(String send,String receive,DatabaseReference databaseReference){
        databaseReference.child("Users").child(send).child("Invite").child(receive).removeValue();
        databaseReference.child("Users").child(receive).child("Invited").child(send).removeValue();
    }


}
