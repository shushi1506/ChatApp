package com.example.shushi.testfirebase;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomAdapter;
import com.example.shushi.models.*;
import com.example.shushi.models.MessageChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;

public class ChatActivity extends AppCompatActivity {
    ImageView btnclick;
    Button btntest;
    EditText txtsend;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listview;
    private int KEY_LOAD = 11;
    DatabaseReference root;
    DatabaseReference rootuser;
    CustomAdapter arrayAdapter;
    ArrayList<MessageChat> arrayList;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        Intent intent = getIntent();
        final String keyFriend = intent.getStringExtra("keyPushFriend");

        arrayList = new ArrayList<MessageChat>();
        btnclick = (ImageView) findViewById(R.id.btnclick);
        //test
        btntest = (Button) findViewById(R.id.btntest);
        imageButton = (ImageButton) findViewById(R.id.call_activity_F2);
        txtsend = (EditText) findViewById(R.id.txtsend);
        listview = (ListView) findViewById(R.id.listView);
//        //khoi tao swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeItem);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KEY_LOAD += 50;
                //refresh();
            }
        });

        arrayAdapter = new CustomAdapter(getApplicationContext(), arrayList);
        //listview.setAdapter(arrayAdapter);
        listview.setAdapter(arrayAdapter);

        arrayAdapter.notifyDataSetChanged();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        root = FirebaseDatabase.getInstance().getReference();

        rootuser = root.child("Users").child(EncodeString(user.getEmail())).child("Friend").child(EncodeString(addSuffixeString(keyFriend))).child("Messages");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final MessageChat messageChat = (MessageChat) arrayAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),"Laew",Toast.LENGTH_SHORT).show();
                final Dialog dialog=new Dialog(ChatActivity.this);
                dialog.setContentView(R.layout.dialog_message);
                dialog.setTitle("");
                Button btnDelete_Mess=(Button)dialog.findViewById(R.id.btnDelete_Message);
                Button btnCopy_Mess=(Button)dialog.findViewById(R.id.btnCopy_Message);
                btnDelete_Mess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMessage(EncodeString(user.getEmail()),EncodeString(addSuffixeString(keyFriend)),root,messageChat.getKey());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                MessageChat ms = new MessageChat();
                ms.setTextMessage(txtsend.getText().toString());
                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
//                addMessage(EncodeString(user.getEmail()),EncodeString(addSuffixeString(item)),keyFriend,root,ms,true);
//                addMessage(EncodeString(addSuffixeString(item)),EncodeString(user.getEmail()),keysave,root,ms,false);
                addMessage(EncodeString(user.getEmail()),EncodeString(addSuffixeString(keyFriend)),root,ms);

//                ms.setMe(true);
//
//                String push = rootuser.push().getKey();
//                ms.setKey(push);
//                rootuser.getParent().getParent().child("lastMessage").setValue(ms.getTextMessage());
//                rootuser.child(push).setValue(ms);
                txtsend.setText("");
                txtsend.isFocused();

                scrollMyListViewToBottom();
            }
        });
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();
                MessageChat ms = new MessageChat();
                ms.setTextMessage(txtsend.getText().toString());
                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                ms.setMe(false);

                String push = rootuser.push().getKey();
                ms.setKey(push);
                ms.setUrlPhoto(push);


                rootuser.child(push).setValue(ms);
                txtsend.setText("");
                txtsend.isFocused();
                scrollMyListViewToBottom();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(ChatActivity.this, PlaceCallActivity.class);
                startActivity(mainActivity);
            }
        });

        rootuser.limitToFirst(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageChat ms1 = dataSnapshot.getValue(MessageChat.class);
                arrayList.add(ms1);
                arrayAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                MessageChat newms = dataSnapshot.getValue(MessageChat.class);
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getKey().equals(key)) {
                        arrayList.set(i, newms);
                        break;
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //them đoi tượng key vào trong class mesages

                String key = dataSnapshot.getKey();
                for (MessageChat ms : arrayList) {
                    if (ms.getKey().equals(key)) {
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
    private void addMessage(String send,String receive,String key,DatabaseReference rootMethod,MessageChat messageChat,boolean me){
        DatabaseReference r = rootMethod.child("Users").child(send).child("Friend").child(key).child(receive).child("Messages");
        String push = r.push().getKey();
        messageChat.setKey(push);
        messageChat.setMe(me);
        r.getParent().getParent().child("lastMessage").setValue(messageChat.getTextMessage());
        r.child(push).setValue(messageChat);
    }

    //new newn new
    private void addMessage(String send,String receive,DatabaseReference rootMethod,MessageChat messageChat){
        DatabaseReference r = rootMethod.child("Users").child(send).child("Friend").child(receive).child("Messages");
        String push = r.push().getKey();
        messageChat.setKey(push);
        messageChat.setMe(true);
        r.child(push).setValue(messageChat);
        messageChat.setMe(false);
        rootMethod.child("Users").child(receive).child("Friend").child(send).child("Messages").child(push).setValue(messageChat);
    }
    private void removeMessage(String send,String receive,DatabaseReference databaseReference,String keyMess){
       databaseReference.child("Users").child(send).child("Friend").child(receive).child("Messages").child(keyMess).removeValue();
    }


}
