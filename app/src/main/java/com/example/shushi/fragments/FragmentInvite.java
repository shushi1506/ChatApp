package com.example.shushi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shushi.adapters.RecyclerViewFindFriendAdapter;
import com.example.shushi.adapters.RecyclerViewFriendAdapter;
import com.example.shushi.models.ProfileInviteModel;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.shushi.supports.SupportString.EncodeString;

/**
 * Created by Shushi on 6/9/2017.
 */

public class FragmentInvite extends Fragment {
    DatabaseReference root;
    RecyclerViewFindFriendAdapter arrayAdapter;
    ArrayList<ProfileInviteModel> arrayList;
    RecyclerView rv;
    FirebaseAuth firebaseAuth;

    public FragmentInvite() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.info_list, container, false);
        arrayList = new ArrayList<ProfileInviteModel>();

        arrayAdapter = new RecyclerViewFindFriendAdapter(arrayList);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(arrayAdapter);

        return rv;
    }

    private void load_data() {
        root = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Query query = root.child("Users").child(EncodeString(firebaseAuth.getCurrentUser().getEmail())).child("Invite");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String keyFriendMess = (String) dataSnapshot.child("keypushFriend").getValue();
                Query queryRef = root.child("Users").child(keyFriendMess).limitToFirst(100);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ProfileInviteModel d = new ProfileInviteModel();
                        d = dataSnapshot.getValue(ProfileInviteModel.class);
                        d.setInvited(true);
                        d.setFriend(false);
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
