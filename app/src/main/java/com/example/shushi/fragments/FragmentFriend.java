package com.example.shushi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomFriendAdapter;
import com.example.shushi.adapters.ItemClickListener;
import com.example.shushi.adapters.RecyclerViewFriendAdapter;
import com.example.shushi.models.FriendModel;
import com.example.shushi.models.MessageChat;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.testfirebase.ChatActivity;
import com.example.shushi.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSufAll;

/**
 * Created by Shushi on 6/7/2017.
 */

public class FragmentFriend extends Fragment  {
    DatabaseReference root;
    RecyclerViewFriendAdapter arrayAdapter;
    ArrayList<ProfileModel> arrayList;
    RecyclerView rv;
    public FragmentFriend() {
    }

    FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.info_list, container, false);
        arrayList = new ArrayList<ProfileModel>();

        arrayAdapter = new RecyclerViewFriendAdapter(arrayList);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(arrayAdapter);

        load_data_test();
//        load_Data_Friend_Model();
//
        return rv;
    }


    public void load_data_test() {
        root = FirebaseDatabase.getInstance().getReference();
        root.keepSynced(true);
        firebaseAuth = FirebaseAuth.getInstance();
        //query find friend
//        Query query=root.child("Users").orderByChild("displayName").equalTo("0155555555");
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(getActivity().getApplicationContext(),"Key day myyyyyyy:"+dataSnapshot.getKey(),Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //query info
//        root.child("Users/01648530887@halo,com/Friend/-Kk8iJWUcB3FTEOvkyNz/keypushFriend").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String name = dataSnapshot.getValue(String.class);
//                Query queryRef = root.child("Users").child(name);
//                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        ProfileModel d = new ProfileModel();
//                        d = dataSnapshot.getValue(ProfileModel.class);
//                        Toast.makeText(getActivity().getApplicationContext(), "Key day myyyyyyy:" + d.getUrlPhoto(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //query test
        Query query = root.child("Users").child(EncodeString(firebaseAuth.getCurrentUser().getEmail())).child("Friend");
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Iterable<DataSnapshot> dataable = dataSnapshot.getChildren();
//                Iterator<DataSnapshot> iterators = dataable.iterator();
//                while (iterators.hasNext()) {
//                    DataSnapshot data = iterators.next();

                String keyFriendMess = (String) dataSnapshot.child("keypushFriend").getValue();
                final Query queryRef = root.child("Users").child(keyFriendMess).limitToFirst(100);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ProfileModel d = new ProfileModel();
                        d = dataSnapshot.getValue(ProfileModel.class);
                        arrayList.add(d);
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String keyFriendMess = (String) dataSnapshot.child("keypushFriend").getValue();
                for (ProfileModel ms : arrayList) {
                    if (addSufAll(ms.getEmailReset()).equals(keyFriendMess)) {
                        arrayList.remove(ms);
                        break;
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void load_Data_Friend_Model() {
        root = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        root.child("Users").child(EncodeString(firebaseAuth.getCurrentUser().getEmail())).child("Friend").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendModel fr = new FriendModel();
                fr = dataSnapshot.getValue(FriendModel.class);
                String keyFriendMess = (String) dataSnapshot.child("keypushFriend").getValue();
                ArrayList<MessageChat> arrayListMess = new ArrayList<MessageChat>();
                try {
                    Iterable<DataSnapshot> dataable = dataSnapshot.child(keyFriendMess).child("Messages").getChildren();
                    Iterator<DataSnapshot> iterators = dataable.iterator();
                    int i = 1;
                    while (iterators.hasNext()) {
                        i++;
                        DataSnapshot data = iterators.next();
                        MessageChat sk = data.getValue(MessageChat.class);
                        if (i == 100) {
                            break;
                        }
                        arrayListMess.add(sk);
                    }
                } catch (Exception e) {
                    Log.d("ds", e.toString());
                }
                fr.setListMessage(arrayListMess);


                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





}
