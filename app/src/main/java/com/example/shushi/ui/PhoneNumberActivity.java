package com.example.shushi.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.testfirebase.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressDialog p;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        auth=FirebaseAuth.getInstance();

        p=new ProgressDialog(this);
        TextView tv=(TextView)findViewById(R.id.txttext_phone_number);
        Button t=(Button)findViewById(R.id.btn_log_out_phone_number) ;
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//       mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                auth.signInWithCredential(phoneAuthCredential);
//                Toast.makeText(getApplicationContext(),auth.getCurrentUser().getPhoneNumber().toString(),Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//            }
//        };
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+841648530887",60, TimeUnit.SECONDS,this,mCallbacks);
    }

}
