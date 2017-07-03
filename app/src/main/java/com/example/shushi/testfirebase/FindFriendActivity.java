package com.example.shushi.testfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shushi.adapters.RecyclerViewFindFriendAdapter;
import com.example.shushi.adapters.RecyclerViewFriendAdapter;
import com.example.shushi.models.FriendModel;
import com.example.shushi.models.ProfileInviteModel;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.supports.SupportString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextFind;
    private ImageView btnBackFind, btnSearchFind;
    private RecyclerView listFind;
    private FirebaseAuth auth;
    private DatabaseReference root;
    private FirebaseUser user;
    RecyclerViewFindFriendAdapter arrayAdapter;
    ArrayList<ProfileInviteModel> arrayList;
    static Boolean has = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        root = FirebaseDatabase.getInstance().getReference();
        editTextFind = (EditText) findViewById(R.id.editTextSearch_FindFriend);
        btnBackFind = (ImageView) findViewById(R.id.btnBack_FindFriend);
        btnSearchFind = (ImageView) findViewById(R.id.btnSearch_FindFriend);
        listFind = (RecyclerView) findViewById(R.id.listViewSearch_FindFriend);

        arrayList = new ArrayList<ProfileInviteModel>();

        arrayAdapter = new RecyclerViewFindFriendAdapter(arrayList);
        listFind.setLayoutManager(new LinearLayoutManager(listFind.getContext()));
        listFind.setAdapter(arrayAdapter);
        btnSearchFind.setOnClickListener(this);
        btnBackFind.setOnClickListener(this);
        listFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == btnSearchFind) {
            String s = editTextFind.getText().toString();

            final Query query = root.child("Users").orderByChild("emailReset").equalTo(s);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    final String key = dataSnapshot.getKey();

                    if (dataSnapshot.exists()) {

                        final Query q = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Friend").child(key);
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String k = dataSnapshot.child("keypushFriend").getValue(String.class);
                                if (dataSnapshot.exists()) {

                                    Query queryRef = root.child("Users").child(key);
                                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ProfileInviteModel d = new ProfileInviteModel();
                                            d = dataSnapshot.getValue(ProfileInviteModel.class);
                                            d.setFriend(true);
                                            d.setInvited(true);
                                            d.setBeInvited(true);
                                            arrayList.clear();
                                            arrayList.add(d);
                                            arrayAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                } else {
                                    final Query qu = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Invite").child(key);
                                    qu.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String kta = dataSnapshot.child("keypushFriend").getValue(String.class);

                                            if (dataSnapshot.exists()) {
                                                Query queryRef = root.child("Users").child(key);
                                                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        ProfileInviteModel d = new ProfileInviteModel();
                                                        d = dataSnapshot.getValue(ProfileInviteModel.class);
                                                        d.setFriend(false);
                                                        d.setInvited(true);
                                                        d.setBeInvited(false);
                                                        arrayList.clear();
                                                        arrayList.add(d);
                                                        arrayAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            } else {
                                                final Query qr = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Invited").child(key);
                                                qr.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String ktp = dataSnapshot.child("keypushFriend").getValue(String.class);
                                                        if (dataSnapshot.exists()) {
                                                            Query queryRef = root.child("Users").child(key);
                                                            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    ProfileInviteModel d = new ProfileInviteModel();
                                                                    d = dataSnapshot.getValue(ProfileInviteModel.class);
                                                                    d.setFriend(false);
                                                                    d.setInvited(false);
                                                                    d.setBeInvited(true);
                                                                    arrayList.clear();
                                                                    arrayList.add(d);
                                                                    arrayAdapter.notifyDataSetChanged();
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        } else {
                                                            Query queryRef = root.child("Users").child(key);
                                                            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    ProfileInviteModel d = new ProfileInviteModel();
                                                                    d = dataSnapshot.getValue(ProfileInviteModel.class);
                                                                    d.setFriend(false);
                                                                    d.setInvited(false);
                                                                    d.setBeInvited(false);
                                                                    arrayList.clear();
                                                                    arrayList.add(d);
                                                                    arrayAdapter.notifyDataSetChanged();
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        qr.removeEventListener(this);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                            qu.removeEventListener(this);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                q.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        arrayList.clear();
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Không tìm thấy", Toast.LENGTH_LONG).show();
                    }
                    query.removeEventListener(this);
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
        if (v == btnBackFind) {
            onBackPressed();
        }
    }

    private void save() {
        final String key = "";
        Boolean email = true;
        if (email) {
            final Query q = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Friend").child(key);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String k = dataSnapshot.child("keypushFriend").getValue(String.class);
                    if (k != null) {
                        Query queryRef = root.child("Users").child(key);
                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ProfileInviteModel d = new ProfileInviteModel();
                                d = dataSnapshot.getValue(ProfileInviteModel.class);
                                d.setFriend(true);
                                d.setInvited(true);
                                d.setBeInvited(true);
                                arrayList.clear();
                                arrayList.add(d);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        final Query qu = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Invite").child(key);
                        qu.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshott) {
                                String kta = dataSnapshott.child("keypushFriend").getValue(String.class);
                                if (kta != null) {
                                    Query queryRef = root.child("Users").child(key);
                                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ProfileInviteModel d = new ProfileInviteModel();
                                            d = dataSnapshot.getValue(ProfileInviteModel.class);
                                            d.setFriend(false);
                                            d.setInvited(true);
                                            d.setBeInvited(false);
                                            arrayList.clear();
                                            arrayList.add(d);
                                            arrayAdapter.notifyDataSetChanged();


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    final Query qr = root.child("Users").child(SupportString.EncodeString(user.getEmail())).child("Invited").child(key);
                                    qr.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String ktp = dataSnapshot.child("keypushFriend").getValue(String.class);
                                            if (ktp != null) {
                                                Query queryRef = root.child("Users").child(key);
                                                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        ProfileInviteModel d = new ProfileInviteModel();
                                                        d = dataSnapshot.getValue(ProfileInviteModel.class);
                                                        d.setFriend(false);
                                                        d.setInvited(false);
                                                        d.setBeInvited(true);
                                                        arrayList.clear();
                                                        arrayList.add(d);
                                                        arrayAdapter.notifyDataSetChanged();


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            } else {
                                                Query queryRef = root.child("Users").child(key);
                                                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        ProfileInviteModel d = new ProfileInviteModel();
                                                        d = dataSnapshot.getValue(ProfileInviteModel.class);
                                                        d.setFriend(false);
                                                        d.setInvited(false);
                                                        d.setBeInvited(false);
                                                        arrayList.clear();
                                                        arrayList.add(d);
                                                        arrayAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            arrayList.clear();
            arrayAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Không tìm thấy", Toast.LENGTH_LONG).show();


        }
    }

    private boolean checkUser(String s) {
        Query query = root.child("Users").orderByChild("emailReset").equalTo(s);

        query.addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getApplicationContext(), "co chay vao change", Toast.LENGTH_LONG).show();

                if (dataSnapshot.exists()) {

                    Toast.makeText(getApplicationContext(), "co chay vao child", Toast.LENGTH_LONG).show();
                  has=true;
                } else{

                    Toast.makeText(getApplicationContext(), "co chay vao else child", Toast.LENGTH_LONG).show();
                    has=false;
                }

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
                Toast.makeText(getApplicationContext(), "co chay vao oncancell", Toast.LENGTH_LONG).show();
            }
        });
        if(has){
            Toast.makeText(getApplicationContext(), "co has", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
