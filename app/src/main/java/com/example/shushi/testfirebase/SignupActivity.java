package com.example.shushi.testfirebase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.example.shushi.models.*;
import com.example.shushi.models.MessageChat;
import com.example.shushi.supports.SupportString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnResetPassword.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnResetPassword) {
            startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
        }
        if (v == btnSignIn) {
            finish();
        }
        if (v == btnSignUp) {
           // region sign up old
            final String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(SupportString.addSuffixeString(email), password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //set info main
                        root = FirebaseDatabase.getInstance().getReference();
                        Calendar today = Calendar.getInstance();
                        ProfileModel fr = new ProfileModel();
                        fr.setDisplayName(email);
                        fr.setEmailReset(email);
                        fr.setConnect(false);
//                        fr.setLastConnect(ServerValue.TIMESTAMP);

                        fr.setUrlPhoto("https://firebasestorage.googleapis.com/v0/b/testfirebase-440b0.appspot.com/o/no-image-icon-hi.png?alt=media&token=8e248aa5-9378-4551-818c-4964838387c0");
                        root.child("Users").child(SupportString.EncodeString(SupportString.addSuffixeString(email))).setValue(fr);
                        final String keyadmin = "01648530887@halo,com";
                        final com.example.shushi.models.MessageChat ms = new com.example.shushi.models.MessageChat();

                        ms.setTextMessage("Đây La Admin quản trị.Chào mừng bạn đến với Halo");
                        ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));

//                        String keysave1 = root.child("Users").child(keyadmin).child("Friend").push().getKey();
//                        final String keysave2 = root.child("Users").child(SupportString.EncodeString(SupportString.addSuffixeString(email))).child("Friend").push().getKey();
                        Update_Account(keyadmin, SupportString.EncodeString(SupportString.addSuffixeString(email)),root,ms,false);
//                        Update_Account_New(keyadmin, SupportString.EncodeString(SupportString.addSuffixeString(email)), keysave1, keysave2, root, ms, false);
//                        Update_Account_New(SupportString.EncodeString(SupportString.addSuffixeString(email)), keyadmin, keysave2, keysave1, root, ms, true);
//

                        if (auth.getCurrentUser() != null) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(email)
                                    .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/testfirebase-440b0.appspot.com/o/no-image-icon-hi.png?alt=media&token=8e248aa5-9378-4551-818c-4964838387c0"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            auth.signOut();
                            Digits.clearActiveSession();
                        }
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            });
          //endregion

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void Update_Account_New(final String userkey1, final String userkey2, final String keysaveMe, final String keysaveFriend, final DatabaseReference rootmethod, final MessageChat mess, final boolean isME) {


        root.child("Users").child(userkey1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileModel pr = new ProfileModel();
                pr = dataSnapshot.getValue(ProfileModel.class);
                DatabaseReference rootMain;
                rootMain = rootmethod.child("Users").child(userkey2).child("Friend");
//                String push = rootMain.push().getKey();
                String pushMess = rootMain.child(keysaveMe).child(userkey2).child("Messages").push().getKey();
                mess.setKey(pushMess);
                mess.setMe(isME);
                Calendar today = Calendar.getInstance();
                FriendModel fM = new FriendModel();
                fM.setDisplayNameFriend(pr.getDisplayName());
                fM.setKeyFriend(keysaveMe);
                fM.setUrlPhoto(pr.getUrlPhoto());
                fM.setTimeConnect(today.getTimeInMillis());
                fM.setLastMessage(mess.getTextMessage());
                fM.setKeySave(keysaveFriend);
                rootMain.child(keysaveMe).setValue(fM);
                rootMain.child(keysaveMe).child("keypushFriend").setValue(userkey1);
//                rootMain.child(keysaveMe).child("keySave").setValue(keysaveFriend);
                rootMain.child(keysaveMe).child(userkey1).child("Messages").child(pushMess).setValue(mess);
                root.child("Users").child(userkey1).removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void Update_Account(final String userkey1, final String userkey2, final DatabaseReference rootmethod, final MessageChat mess, final boolean isME) {
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
