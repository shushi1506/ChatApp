package com.example.shushi.testfirebase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.adapters.CustomAdapter;
import com.example.shushi.models.*;
import com.example.shushi.models.MessageChat;
import com.example.shushi.ui.ActionItem;
import com.example.shushi.ui.QuickActionPopup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.shushi.supports.SupportString.CreatePassword;
import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;
import static com.example.shushi.supports.SupportString.millisecondsToString;
import static com.example.shushi.supports.SupportString.subSuffixeString;
import static com.example.shushi.testfirebase.AudioPlayer.LOG_TAG;

public class ChatActivity extends BaseActivity implements SinchService.StartFailedListener {
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
    QuickActionPopup quickAction1;
    private FirebaseUser user;
    private String keyFriend, urlImgFriend, urlImgUser;
    private ImageView dragRecord, dropRecord, sendRecord;

    private ProgressDialog progressDialog;
    private static final int ID_DELETE = 1;
    private static final int ID_IMAGE = 1;
    private static final int ID_VOICE = 2;
    private static final int ID_RECORD = 3;
    private static final int GALARY_INTENT = 1;
    private static final int FILE_CODE = 111;
    private MediaRecorder mRecorder = null;
    private static String mFileName = null;
    private boolean isSpeakButtonLongPressed = false;
    private boolean isDrop = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_two);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        Intent intent = getIntent();
        keyFriend = intent.getStringExtra("keyPushFriend");
        urlImgFriend = intent.getStringExtra("urlImgFriend");
        user = FirebaseAuth.getInstance().getCurrentUser();
        urlImgUser = user.getPhotoUrl().toString();
        root = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecord.3gp";
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
                    View v = quickAction.getViewItemParent();
                    v.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                    final MessageChat messageChat = (MessageChat) arrayAdapter.getItem(quickAction.getPosItemParent());
                    removeMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, messageChat.getKey());
                    quickAction.dismiss();
                }
            }
        });

        //set dismiss listener
        quickAction.setOnDismissListener(new QuickActionPopup.OnDismissListener() {
            @Override
            public void onDismiss() {
                View v = quickAction.getViewItemParent();
                v.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
            }
        });
        ActionItem imageItem = new ActionItem(ID_IMAGE, "Image", getResources().getDrawable(R.drawable.ic_action_picture));
        ActionItem voiceItem = new ActionItem(ID_VOICE, "Voice", getResources().getDrawable(R.drawable.ic_action_ring_volume));
        ActionItem recordItem = new ActionItem(ID_RECORD, "Record", getResources().getDrawable(R.drawable.ic_action_mic));
        quickAction1 = new QuickActionPopup(this, QuickActionPopup.HORIZONTAL);
        quickAction1.addActionItem(imageItem);
        quickAction1.addActionItem(voiceItem);
//        quickAction1.addActionItem(recordItem);

        quickAction1.setOnActionItemClickListener(new QuickActionPopup.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActionPopup source, int pos, int actionId) {
                if (actionId == ID_IMAGE) {
                    Intent in = new Intent(Intent.ACTION_PICK);
                    in.setType("image/*");
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(in, GALARY_INTENT);
                } else if (actionId == ID_VOICE) {
                    Toast.makeText(getApplicationContext(), "Gọi", Toast.LENGTH_SHORT).show();
                    callButtonClicked();
                } else if (actionId == ID_RECORD) {
//                    Toast.makeText(getApplicationContext(), "Giữ để ghi âm", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("application/*");
//                    startActivityForResult(intent, FILE_CODE);
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

        sendRecord = (ImageView) findViewById(R.id.sendRecordChat);
        dragRecord = (ImageView) findViewById(R.id.dragRecordChat);
        dropRecord = (ImageView) findViewById(R.id.dropRecordChat);
        dropRecord.setOnDragListener(new MyDragListener());
        sendRecord.setOnLongClickListener(new MyLongClickListener());
        sendRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Giữ để ghi âm", Toast.LENGTH_LONG).show();

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KEY_LOAD += 50;
                //refresh();
            }
        });
        arrayAdapter = new CustomAdapter(getApplicationContext(), arrayList, urlImgUser, urlImgFriend);
        listview.setAdapter(arrayAdapter);
//        registerForContextMenu(listview);

        arrayAdapter.notifyDataSetChanged();


        rootuser = root.child("Users").child(EncodeString(user.getEmail())).child("Friend").child(EncodeString(addSuffixeString(keyFriend))).child("Messages");
        rootuser.keepSynced(true);
        //region code click listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//                MessageChat m= (MessageChat) arrayAdapter.getItem(position);
//                if(m.getUrlPhoto()!=null&&!m.isAudio()){
//                    Toast.makeText(getApplicationContext(),m.getUrlPhoto(),Toast.LENGTH_LONG).show();
//                }
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
                quickAction.show(view, position);
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                ms.setAudio(false);
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
        quickAction1.dismiss();
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file_message, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        quickAction1.show(findViewById(R.id.chose_file_send), 0);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_INTENT && data != null) {
            //region up load image
            Uri uri = data.getData();
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            String child;

            if (uri.getLastPathSegment() == null) {
                child = CreatePassword(12);
            } else {
                child = CreatePassword(12) + uri.getLastPathSegment();
            }
            final StorageReference storageChile = mStorage.child("Photos").child(child);

// Listen for state changes, errors, and completion of the upload.
            storageChile.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Toast.makeText(getApplicationContext(), "Progress:" + progress, Toast.LENGTH_SHORT).show();

                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "pause", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Error:" + exception.toString(), Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
                    progressDialog.dismiss();
                    Uri u = taskSnapshot.getDownloadUrl();
                    Calendar today = Calendar.getInstance();
                    MessageChat ms = new MessageChat();
                    ms.setTextMessage("");
                    ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                    ms.setUrlPhoto(u.toString());
                    addMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, ms);
                    txtsend.setText("");
                    txtsend.isFocused();
                    scrollMyListViewToBottom();
                    Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_SHORT).show();
                }
            });
            //endregion image
        }
        else if(requestCode == FILE_CODE && data != null){
            Toast.makeText(getApplicationContext(), "ok" , Toast.LENGTH_SHORT).show();
            //region up load file
            Uri uri = data.getData();

            final String type= getMimeType(uri);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            String child;

            if (uri.getLastPathSegment() == null) {
                child = CreatePassword(12)+type;
            } else {
                child = CreatePassword(12) + uri.getLastPathSegment()+type;
            }
            final StorageReference storageChile = mStorage.child("Docs").child(child);

// Listen for state changes, errors, and completion of the upload.
            storageChile.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    Toast.makeText(getApplicationContext(), "Progress:" + progress, Toast.LENGTH_SHORT).show();

                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "pause", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Error:" + exception.toString(), Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
                    progressDialog.dismiss();
                    Uri u = taskSnapshot.getDownloadUrl();
                    Calendar today = Calendar.getInstance();
                    MessageChat ms = new MessageChat();
                    ms.setTextMessage("");
                    ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                    String link="<a href=\""+u.toString()+"\">"+u.getLastPathSegment()+"</a>";
                    ms.setUrlPhoto(link);
                    ms.setDoc(true);
                    addMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, ms);
                    txtsend.setText("");
                    txtsend.isFocused();
                    scrollMyListViewToBottom();
                    Toast.makeText(getApplicationContext(), "Success" , Toast.LENGTH_SHORT).show();
                }
            });
            //endregion
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }
    private void callButtonClicked() {
        final String userName = keyFriend;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            final Call call = getSinchServiceInterface().callUser(userName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
//                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
//                        + "placing a call.", Toast.LENGTH_LONG).show();
                getSinchServiceInterface().stopClient();
                if(getSinchServiceInterface().isStarted()){
                }else {
                    getSinchServiceInterface().startClient(subSuffixeString(user.getEmail()));
                }
                Timer t=new Timer();
                TimerTask task=new TimerTask() {
                    @Override
                    public void run() {
                        Call call = getSinchServiceInterface().callUser(userName);
                        String callId = call.getCallId();
                        Intent callScreen = new Intent(ChatActivity.this, CallScreenActivity.class);
                        callScreen.putExtra(SinchService.CALL_ID, callId);
                        startActivity(callScreen);
                    }
                };
                t.schedule(task,1000);
            }else {
                String callId = call.getCallId();
                Intent callScreen = new Intent(this, CallScreenActivity.class);
                callScreen.putExtra(SinchService.CALL_ID, callId);
                startActivity(callScreen);
            }
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }

    }

    public class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DRAG_LOCATION:

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Do nothing
                    if (isDrop == true) {
                        isDrop = false;
                        dragRecord.setVisibility(View.GONE);
                        dropRecord.setVisibility(View.GONE);
                        stopRecording();
                    } else {
                        dropRecord.setVisibility(View.GONE);
                        isDrop = false;
                        stopRecording();
                        upLoadRecord();
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    final View view = (View) event.getLocalState();
                    v.setVisibility(View.GONE);
                    isDrop = true;
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void upLoadRecord() {
        progressDialog.setMessage("Uploading audio...");
        progressDialog.show();
        StorageReference child=mStorage.child("Audio").child(CreatePassword(12)+"audio.3gp");
        Uri file=Uri.fromFile(new File(mFileName));
        child.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               progressDialog.dismiss();
                Uri u = taskSnapshot.getDownloadUrl();
                Calendar today = Calendar.getInstance();
                MessageChat ms = new MessageChat();
                ms.setTextMessage("");
                ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));
                ms.setUrlPhoto(u.toString());
                ms.setAudio(true);
                addMessage(EncodeString(user.getEmail()), EncodeString(addSuffixeString(keyFriend)), root, ms);
                txtsend.setText("");
                txtsend.isFocused();
                scrollMyListViewToBottom();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final class MyLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            dropRecord.setVisibility(View.VISIBLE);
            isDrop = false;
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    sendRecord);
            v.startDrag(data, shadowBuilder, v, 0);
            startRecording();
            return true;
        }
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
