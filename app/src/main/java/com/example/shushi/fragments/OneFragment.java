package com.example.shushi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomFriendAdapter;
import com.example.shushi.models.FriendModel;
import com.example.shushi.adapters.CustomAdapter;
import com.example.shushi.models.LastMessage;
import com.example.shushi.models.MessageChat;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.supports.SupportString;
import com.example.shushi.testfirebase.ChatActivity;
import com.example.shushi.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.shushi.supports.SupportString;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.shushi.supports.SupportString.DecodeString;
import static com.example.shushi.supports.SupportString.EncodeString;


public class OneFragment extends Fragment {

    private static final int PICK_CONTACT_REQUEST = 1;
    DatabaseReference root;
    CustomFriendAdapter arrayAdapter;
    ArrayList<FriendModel> arrayList;

    FirebaseAuth firebaseAuth;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ListView lv = (ListView) view.findViewById(R.id.listViewFriend);

        arrayList = new ArrayList<FriendModel>();

        arrayAdapter = new CustomFriendAdapter(getContext(), arrayList);
        lv.setAdapter(arrayAdapter);


        load_Data_Friend_Model();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendModel friendModel = (FriendModel) arrayAdapter.getItem(position);
                Intent pickIntent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
                pickIntent.putExtra("Item", friendModel.getDisplayNameFriend());
                pickIntent.putParcelableArrayListExtra("List", friendModel.getListMessage());
                pickIntent.putExtra("keyFriend", friendModel.getKeyFriend());
                pickIntent.putExtra("keySave",friendModel.getKeySave());

                ArrayList<FriendModel> dw=arrayAdapter.getFriendList();
                Toast.makeText(getContext(),dw.get(4).getDisplayNameFriend().toString(),Toast.LENGTH_LONG).show();
                getActivity().startActivity(pickIntent);
            }
        });
        return view;
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

                arrayList.add(fr);
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
