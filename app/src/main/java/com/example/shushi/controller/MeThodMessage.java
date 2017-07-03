package com.example.shushi.controller;

import com.example.shushi.models.MessageChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by Shushi on 6/11/2017.
 */

public class MeThodMessage {

    public static void addFriend(final String userkey1, final String userkey2, final DatabaseReference rootmethod, final MessageChat mess, final boolean isME) {
        String pushMess = rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("Messages").push().getKey();
        mess.setKey(pushMess);
        mess.setMe(isME);
        rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("Messages").child(pushMess).setValue(mess);
        rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("keypushFriend").setValue(userkey2);
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("Messages").child(pushMess).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MessageChat p = mutableData.getValue(MessageChat.class);
                if (p == null) {
                    mutableData.setValue(mess);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String p = mutableData.getValue(String.class);
                if (p == null) {
                    mutableData.setValue(userkey1);
                }
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    public static void sendInvite(String send, String receive, DatabaseReference databaseReference) {
        databaseReference.child("Users").child(send).child("Invite").child(receive).child("keypushFriend").setValue(receive);
        databaseReference.child("Users").child(receive).child("Invited").child(send).child("keypushFriend").setValue(send);
    }

    public static void deleteInvite(String send, String receive, DatabaseReference databaseReference) {
        databaseReference.child("Users").child(send).child("Invite").child(receive).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String p = mutableData.getValue(String.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                mutableData.setValue(null);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        databaseReference.child("Users").child(receive).child("Invited").child(send).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }


    public static void deleteFriend(String send, String receive, DatabaseReference databaseReference) {
        databaseReference.child("Users").child(send).child("Friend").child(receive).removeValue();
        databaseReference.child("Users").child(receive).child("Friend").child(send).removeValue();
    }

    public static void addFriendWithInvite(final String userkey1, final String userkey2, final DatabaseReference rootmethod, final MessageChat mess, final boolean isME) {
        String pushMess = rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("Messages").push().getKey();
        mess.setKey(pushMess);
        mess.setMe(isME);
        rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String p = mutableData.getValue(String.class);
                if (p == null) {
                    mutableData.setValue(userkey2);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

        rootmethod.child("Users").child(userkey1).child("Friend").child(userkey2).child("Messages").child(pushMess).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MessageChat p = mutableData.getValue(MessageChat.class);
                if (p == null) {
                    mutableData.setValue(mess);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String p = mutableData.getValue(String.class);
                if (p == null) {
                    mutableData.setValue(userkey1);
                }
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("Messages").child(pushMess).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MessageChat p = mutableData.getValue(MessageChat.class);
                if (p == null) {
                    mutableData.setValue(mess);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        rootmethod.child("Users").child(userkey2).child("Invite").child(userkey1).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                String p = mutableData.getValue(String.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                mutableData.setValue(null);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
        rootmethod.child("Users").child(userkey1).child("Invited").child(userkey2).child("keypushFriend").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

    }

    public static void Update_Account(final String userkey1, final String userkey2, final DatabaseReference rootmethod, final MessageChat mess, final boolean isME) {
        DatabaseReference rootMain;
        rootMain = rootmethod.child("Users").child(userkey1).child("Friend");
        String pushMess = rootMain.child(userkey2).child("Messages").push().getKey();
        mess.setKey(pushMess);
        mess.setMe(isME);
        rootMain.child(userkey2).child("Messages").child(pushMess).setValue(mess);
        rootMain.child(userkey2).child("keypushFriend").setValue(userkey2);
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("Messages").child(pushMess).setValue(mess);
        rootmethod.child("Users").child(userkey2).child("Friend").child(userkey1).child("keypushFriend").setValue(userkey1);
    }

}
