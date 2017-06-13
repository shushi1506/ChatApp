package com.example.shushi.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomAdapter;

import com.example.shushi.models.MessageChat;
import com.example.shushi.testfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import static com.example.shushi.supports.SupportString.EncodeString;

/**
 * Created by Shushi on 4/23/2017.
 */

public class TwoFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference root;
    DatabaseReference rootuser;
    DatabaseReference rootfriend;
    ImageView btnclick;
    Button btntest;
    EditText txtsend;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listview;
    private int KEY_LOAD = 11;

    CustomAdapter arrayAdapter;
    ArrayList<MessageChat> arrayList;
    public TwoFragment() {
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
        View view=inflater.inflate(R.layout.fragment_two, container, false);
        btnclick = (ImageView)view.findViewById(R.id.btnclick);
        //test


        txtsend = (EditText) view.findViewById(R.id.txtsend);
        listview = (ListView) view.findViewById(R.id.listView);
//        //khoi tao swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeItem);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KEY_LOAD += 50;
                refresh();
            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //khoi tao firebase

        root = FirebaseDatabase.getInstance().getReference();

        rootuser = root.child("Users").child(EncodeString(user.getEmail())).child("Friend").child("-Kk6szQ7DIiDESu4-xsD").child("12345@halo,com").child("Messages");
//        rootfriend= root.child("Users").child("01648530886").child("Friend").child("01648530885").child("Messages");
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                MessageChat ms = new MessageChat();
                ms.setTextMessage(txtsend.getText().toString());
                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                ms.setMe(true);

                String push=rootuser.push().getKey();
                ms.setKey(push);
              //  ms.setUrlPhoto(push);
//               Toast.makeText(getContext(),"push:"+ms.getKey(), Toast.LENGTH_SHORT).show();
//
//                root.child("Users").child("01648530887@HaLo,com").child("Friend").child("-KgPiMP-Y_CNsh3v2sQR").child("lastMessage").setValue(ms.getTextMessage());
//                rootuser.child(push).setValue(ms);
//                ms.setMe(false);
//                rootfriend.child(push).setValue(ms);
                txtsend.setText("");
                txtsend.isFocused();
               // listview.setStackFromBottom(true);
                scrollMyListViewToBottom();
            }
        });

        //region btn test
        //        btntest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar today = Calendar.getInstance();
//                MessageChat ms = new MessageChat();
//                ms.setTextMessage(txtsend.getText().toString());
//                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
//                ms.setMe(false);
//
//                String push=rootuser.push().getKey();
//                ms.setKey(push);
//                 ms.setUrlPhoto(push);
//                Toast.makeText(getContext(),"push:"+ms.getKey(), Toast.LENGTH_SHORT).show();
//
//                root.child("Users").child("01648530887@HaLo,com").child("Friend").child("-KgPiMP-Y_CNsh3v2sQR").child("lastMessage").setValue(ms.getTextMessage());
//                rootuser.child(push).setValue(ms);
//                ms.setMe(true);
//                rootfriend.child(push).setValue(ms);
//                txtsend.setText("");
//                txtsend.isFocused();
//                // listview.setStackFromBottom(true);
//                scrollMyListViewToBottom();
//            }
//        });
        //endregion
//        //khoi tao array cho listview
//
//
        arrayList = new ArrayList<MessageChat>();
        arrayAdapter = new CustomAdapter(this.getContext(), arrayList);
        //listview.setAdapter(arrayAdapter);
        listview.setAdapter(arrayAdapter);

        //load tin nhan ra listview

//        //su kien bat messager send
        rootuser.limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageChat  ms1 = dataSnapshot.getValue(MessageChat.class);
               // Toast.makeText(getContext(),"puaddchild:"+ms1.getKey(), Toast.LENGTH_SHORT).show();
                arrayList.add(ms1);
                arrayAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key=dataSnapshot.getKey();
                MessageChat newms= dataSnapshot.getValue(MessageChat.class);
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).getKey().equals(key)){
                        arrayList.set(i,newms);
                        break;
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //them đoi tượng key vào trong class mesages

                String key=dataSnapshot.getKey();
                for(MessageChat ms:arrayList){
                    if(ms.getKey().equals(key)){
                        arrayList.remove(ms);
                        break;
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        //su kien click cho listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageChat ms111=(MessageChat) arrayAdapter.getItem(position);
                Toast.makeText(getContext(),"Short"+ms111.getKey()+"xxx:"+position, Toast.LENGTH_SHORT).show();
            }
        });
        //su ki8en nhan va giu
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageChat ms111=(MessageChat) arrayAdapter.getItem(position);
           //     Toast.makeText(getContext(),"Long"+ms111.getText()+ms111.getTime(), Toast.LENGTH_SHORT).show();
                return  false;
            }
        });


        return view;
    }


    private void refresh() {
        rootuser.limitToLast(KEY_LOAD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> dataSnapshotIterator = snapshotIterator.iterator();
                while (dataSnapshotIterator.hasNext()) {
                    DataSnapshot snapshot = dataSnapshotIterator.next();
                    MessageChat ms = new MessageChat();
                    ms = snapshot.getValue(MessageChat.class);
                    arrayList.add(ms);
                    // name.add(ms.getText());

                }
                arrayAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
                scrollMyListViewToBottom();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }
}
