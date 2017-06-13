package com.example.shushi.testfirebase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.shushi.adapters.CustomAdapter;
import com.example.shushi.models.*;
import com.example.shushi.models.MessageChat;
import com.example.shushi.ui.ActionItem;
import com.example.shushi.ui.QuickActionPopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;

public class ChatActivity extends AppCompatActivity {
    ImageView btnclick;
    EditText txtsend;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listview;
    private int KEY_LOAD = 11;
    DatabaseReference root;
    DatabaseReference rootuser;
    CustomAdapter arrayAdapter;
    ArrayList<MessageChat> arrayList;
    private StorageReference mStorage;
    private ImageButton fileButton;
    QuickActionPopup quickAction1;
    private FirebaseUser user;
    private String keyFriend;

    private ProgressDialog progressDialog;
    private static final int ID_DELETE = 1;
    private static final int ID_IMAGE = 1;
    private static final int ID_VOICE = 2;
    private static final int GALARY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        Intent intent = getIntent();
        keyFriend = intent.getStringExtra("keyPushFriend");
        user = FirebaseAuth.getInstance().getCurrentUser();
        root = FirebaseDatabase.getInstance().getReference();
        mStorage= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        //region code init quickaction
        ActionItem deleteItem = new ActionItem(ID_DELETE, "Delete", getResources().getDrawable(R.drawable.ic_action_remove));
        final QuickActionPopup quickAction = new QuickActionPopup(this, QuickActionPopup.HORIZONTAL);

        //add action items into QuickActionPopup
        quickAction.addActionItem(deleteItem);


        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickActionPopup.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActionPopup source, int pos, int actionId) {

                //filtering items by id
                if (actionId == ID_DELETE) {
                    Toast.makeText(getApplicationContext(), "Mail clicked", Toast.LENGTH_SHORT).show();
                    View v=quickAction.getViewItemParent();
                    v.setBackgroundColor (getResources().getColor(R.color.tw__transparent));
                    final MessageChat messageChat = (MessageChat) arrayAdapter.getItem(quickAction.getPosItemParent());
                    removeMessage(EncodeString(user.getEmail()),EncodeString(addSuffixeString(keyFriend)),root,messageChat.getKey());

                    quickAction.dismiss();
                }
            }
        });

        //set dismiss listener
        quickAction.setOnDismissListener(new QuickActionPopup.OnDismissListener() {
            @Override
            public void onDismiss() {
                View v=quickAction.getViewItemParent();
                v.setBackgroundColor (getResources().getColor(R.color.tw__transparent));
            }
        });
        ActionItem imageItem = new ActionItem(ID_IMAGE, "Image", getResources().getDrawable(R.drawable.ic_action_picture));
        ActionItem voiceItem = new ActionItem(ID_VOICE, "Voice", getResources().getDrawable(R.drawable.ic_action_mic));
        quickAction1  = new QuickActionPopup(this, QuickActionPopup.HORIZONTAL);
        quickAction1.addActionItem(imageItem);
        quickAction1.addActionItem(voiceItem);
        quickAction1.setOnActionItemClickListener(new QuickActionPopup.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActionPopup source, int pos, int actionId) {
                if (actionId == ID_IMAGE) {
                    Intent in=new Intent(Intent.ACTION_PICK);
                    in.setType("image/*");
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(in,GALARY_INTENT);
                } else {
                    Toast.makeText(getApplicationContext(), "Voice clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region code init toobar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        arrayList = new ArrayList<MessageChat>();
        btnclick = (ImageView) findViewById(R.id.btnclick);
        txtsend = (EditText) findViewById(R.id.txtsend);
        listview = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeItem);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KEY_LOAD += 50;
                //refresh();
            }
        });
        arrayAdapter = new CustomAdapter(getApplicationContext(), arrayList);
        listview.setAdapter(arrayAdapter);
//        registerForContextMenu(listview);

        arrayAdapter.notifyDataSetChanged();


        rootuser = root.child("Users").child(EncodeString(user.getEmail())).child("Friend").child(EncodeString(addSuffixeString(keyFriend))).child("Messages");

        //region code click listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //region code show dialog
//                final MessageChat messageChat = (MessageChat) arrayAdapter.getItem(position);
//                Toast.makeText(getApplicationContext(),"Laew",Toast.LENGTH_SHORT).show();
//                final Dialog dialog=new Dialog(ChatActivity.this);
//                dialog.setContentView(R.layout.dialog_message);
//                dialog.setTitle("");
//                Button btnDelete_Mess=(Button)dialog.findViewById(R.id.btnDelete_Message);
//                Button btnCopy_Mess=(Button)dialog.findViewById(R.id.btnCopy_Message);
//                btnDelete_Mess.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        removeMessage(EncodeString(user.getEmail()),EncodeString(addSuffixeString(keyFriend)),root,messageChat.getKey());
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                return true;
                //endregion
                //region code show popup normal
//                doPopup();
                //endregion
                view.setBackgroundColor(getResources().getColor(R.color.com_facebook_button_background_color_selected));
                quickAction.show(view,position);


                return true;
            }
        });
        //endregion
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region code add mess
                Calendar today = Calendar.getInstance();
                MessageChat ms = new MessageChat();
                ms.setTextMessage(txtsend.getText().toString().trim());
                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                addMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, ms);
                txtsend.setText("");
                txtsend.isFocused();
                scrollMyListViewToBottom();
                //endregion

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

    private void doPopup() {
        PopupMenu popupMenu = new PopupMenu(this, listview);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_delete_mess:
                        Toast.makeText(getApplicationContext(), "Delete Clicked", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.popup_edit_mess:
                        Toast.makeText(getApplicationContext(), "Edit Clicked", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_popup_message, popupMenu.getMenu());
        popupMenu.show();
    }


    //region  code context menu
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        if (v.getId()==R.id.listView) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
//            menu.add(0, v.getId(), 0, "Call");
//
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        int menuItemIndex = item.getItemId();
//        Toast.makeText(getApplicationContext(), "Clicked"+menuItemIndex, Toast.LENGTH_LONG).show();
//        switch (item.getItemId()) {
//            case 0:
//                Toast.makeText(getApplicationContext(), "Edit Clicked", Toast.LENGTH_LONG).show();
//                break;
//            case 1:
//                Toast.makeText(getApplicationContext(), "Delete Clicked", Toast.LENGTH_LONG).show();
//                break;
//        }
//
//        return true;
//    }
    //endregion

    private void scrollMyListViewToBottom() {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    private void addMessage(String send, String receive, String key, DatabaseReference rootMethod, MessageChat messageChat, boolean me) {
        DatabaseReference r = rootMethod.child("Users").child(send).child("Friend").child(key).child(receive).child("Messages");
        String push = r.push().getKey();
        messageChat.setKey(push);
        messageChat.setMe(me);
        r.getParent().getParent().child("lastMessage").setValue(messageChat.getTextMessage());
        r.child(push).setValue(messageChat);
    }

    //new newn new
    private void addMessage(String send, String receive, DatabaseReference rootMethod, MessageChat messageChat) {
        DatabaseReference r = rootMethod.child("Users").child(send).child("Friend").child(receive).child("Messages");
        String push = r.push().getKey();
        messageChat.setKey(push);
        messageChat.setMe(true);
        r.child(push).setValue(messageChat);
        messageChat.setMe(false);
        rootMethod.child("Users").child(receive).child("Friend").child(send).child("Messages").child(push).setValue(messageChat);
    }

    private void removeMessage(String send, String receive, DatabaseReference databaseReference, String keyMess) {
        databaseReference.child("Users").child(send).child("Friend").child(receive).child("Messages").child(keyMess).removeValue();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file_message, menu);
//        fileButton = (ImageButton) menu.findItem(R.id.chose_file_send).getActionView();
//        fileButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//               quickAction1.show(v);
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        quickAction1.show(findViewById(R.id.chose_file_send),0);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALARY_INTENT && data.getData()!=null){
             Uri uri=data.getData();
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            final StorageReference storageChile=mStorage.child("Photos").child(uri.getLastPathSegment());
            storageChile.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    Uri u = taskSnapshot.getDownloadUrl();
//                    Calendar today = Calendar.getInstance();
//                    MessageChat ms = new MessageChat();
//                    ms.setTextMessage("");
//                    ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
//                    ms.setUrlPhoto(u.toString());
//                    addMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, ms);
//                    txtsend.setText("");
//                    txtsend.isFocused();
//                    scrollMyListViewToBottom();
                    Toast.makeText(getApplicationContext(), "Done"+u.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
