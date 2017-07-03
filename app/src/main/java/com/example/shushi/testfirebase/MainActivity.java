package com.example.shushi.testfirebase;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.shushi.models.MessageChat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference root;
    DatabaseReference rootuser;
    ImageView btnclick;
    EditText txtsend;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listview;
    private int KEY_LOAD = 11;

    CustomAdapter arrayAdapter;
    ArrayList<MessageChat> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnclick = (ImageView) findViewById(R.id.btnclick);

        txtsend = (EditText) findViewById(R.id.txtsend);
        listview = (ListView) findViewById(R.id.listView);
        //khoi tao swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeItem);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KEY_LOAD += 5;
                refresh();
            }
        });
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //khoi tao firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        root = FirebaseDatabase.getInstance().getReference();

        rootuser = root.child("Users").child("01648530885").child("Friend").child("01648530886").child("Messages");
        rootuser.keepSynced(true);
        btnclick.setOnClickListener(this);

        //khoi tao array cho listview


        arrayList = new ArrayList<MessageChat>();
//        arrayAdapter = new CustomAdapter(this.getApplicationContext(), arrayList);
        //listview.setAdapter(arrayAdapter);
        listview.setAdapter(arrayAdapter);
      
        //load tin nhan ra listview
        
        rootuser.limitToLast(KEY_LOAD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

                listview.setStackFromBottom(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //su kien bat messager send
        rootuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rootuser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageChat ms = new MessageChat();
                ms = dataSnapshot.getValue(MessageChat.class);
                arrayList.add(ms);
                arrayAdapter.notifyDataSetChanged();

                listview.setStackFromBottom(true);
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

        root.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String index = "";
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> dataSnapshotIterator = snapshotIterator.iterator();
                while (dataSnapshotIterator.hasNext()) {
                    DataSnapshot snapshot = dataSnapshotIterator.next();
                    index+=(String)snapshot.getKey()+"\n";
                }

                Toast.makeText(getApplicationContext(),"Key:"+index, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        //su kien click cho listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageChat ms111=(MessageChat) arrayAdapter.getItem(position);
             //   Toast.makeText(getApplicationContext(),"Short"+ms111.getText()+ms111.getTime(), Toast.LENGTH_SHORT).show();
            }
        });
        //su ki8en nhan va giu
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageChat ms111=(MessageChat) arrayAdapter.getItem(position);
            //    Toast.makeText(getApplicationContext(),"Long"+ms111.getText()+ms111.getTime(), Toast.LENGTH_SHORT).show();
                return  false;
            }
        });


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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //code test

    @Override
    public void onClick(View v) {
        if (v == btnclick) {
            Toast.makeText(this.getApplicationContext(), "?c", Toast.LENGTH_SHORT).show();
            //  Map<String,String> map=new HashMap<>();
            //  map.put("text",txtsend.getText().toString().trim());
            //  map.put("time", "11/2/2017");
            Calendar today = Calendar.getInstance();
         //   MessageChat ms = new MessageChat(txtsend.getText().toString().trim(), today.getTime().toString());

           // rootuser.push().setValue(ms);


        }
    }



}

