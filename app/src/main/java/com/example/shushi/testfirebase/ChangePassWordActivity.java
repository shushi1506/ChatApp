package com.example.shushi.testfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.example.shushi.account_kit.AccountKitActivity;
import com.example.shushi.controller.MeThodMessage;
import com.example.shushi.models.ProfileModel;
import com.example.shushi.supports.SupportString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ChangePassWordActivity extends AppCompatActivity {
    private EditText pass, confirmpass;
    private Button btn_update_pass, btn_back;
    private String number;
    private FirebaseAuth auth;
    private DatabaseReference root;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
        auth = FirebaseAuth.getInstance();
        pass = (EditText) findViewById(R.id.password_reset);
        mProgressDialog = new ProgressDialog(this);
        confirmpass = (EditText) findViewById(R.id.confirm_password_reset);
        btn_update_pass = (Button) findViewById(R.id.btn_update_password);
        btn_back = (Button) findViewById(R.id.btn_back);

        Digits.clearActiveSession();
        btn_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String passd = pass.getText().toString();
                String confrim_pas = confirmpass.getText().toString();
                if (TextUtils.isEmpty(passd)) {
                    Toast.makeText(getApplicationContext(), "Nhập mật khẩu mới", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(confrim_pas)) {
                    Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu mới", Toast.LENGTH_LONG).show();
                    confirmpass.requestFocus();
                    return;
                }
                if (passd.equals(confrim_pas)) {
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.show();
                    final FirebaseUser user = auth.getCurrentUser();

                    root = FirebaseDatabase.getInstance().getReference();
                    Calendar today = Calendar.getInstance();
                    final ProfileModel fr = new ProfileModel();
                    fr.setDisplayName(SupportString.DecodeStringNumber(number));
                    fr.setEmailReset(SupportString.DecodeStringNumber(number));
                    fr.setConnect(false);
                    fr.setUrlPhoto("https://firebasestorage.googleapis.com/v0/b/testfirebase-440b0.appspot.com/o/no-image-icon-hi.png?alt=media&token=8e248aa5-9378-4551-818c-4964838387c0");
                    final String keyadmin = "01648530887@halo,com";
                    final com.example.shushi.models.MessageChat ms = new com.example.shushi.models.MessageChat();

                    ms.setTextMessage("Đây La Admin quản trị.Chào mừng bạn đến với Halo");
                    ms.setTimeMessage(String.valueOf(today.getTimeInMillis()));

                    auth.createUserWithEmailAndPassword(SupportString.addSuffixeString(SupportString.DecodeStringNumber(number)), passd).addOnCompleteListener(ChangePassWordActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                root.child("Users").child(SupportString.EncodeString(SupportString.addSuffixeString(SupportString.DecodeStringNumber(number)))).setValue(fr);
                                MeThodMessage.Update_Account(keyadmin, SupportString.EncodeString(SupportString.addSuffixeString(SupportString.DecodeStringNumber(number))), root, ms, false);
                                auth.signOut();
                                Timer t = new Timer();
                                TimerTask task1 = new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (auth.getCurrentUser() == null) {
                                            mProgressDialog.dismiss();
                                            startActivity(new Intent(ChangePassWordActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                        else {

                                        }
                                    }
                                };
                                t.schedule(task1, 4000);
                            }else {
                                Toast.makeText(getApplicationContext(), "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//                    AuthCredential credential = EmailAuthProvider.getCredential(email, "123456");

//                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                user.updatePassword(passd).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (!task.isSuccessful()) {
//
//                                        } else {
//                                            root.child("Users").child(SupportString.EncodeString(SupportString.addSuffixeString(SupportString.DecodeStringNumber(number)))).setValue(fr);
//                                            MeThodMessage.Update_Account(keyadmin, SupportString.EncodeString(SupportString.addSuffixeString(SupportString.DecodeStringNumber(number))), root, ms, false);
//                                            auth.signOut();
//                                            Timer t = new Timer();
//                                            TimerTask task1 = new TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    if (auth.getCurrentUser() == null) {
//                                                        mProgressDialog.dismiss();
//                                                        startActivity(new Intent(ChangePassWordActivity.this, LoginActivity.class));
//                                                        finish();
//                                                    }
//                                                    else {
//
//                                                    }
//                                                }
//                                            };
//                                            t.schedule(task1, 2000);
//
//                                        }
//                                    }
//                                });
//                            } else {
//
//                            }
//                        }
//                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
