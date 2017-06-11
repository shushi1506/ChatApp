package com.example.shushi.account_kit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.example.shushi.supports.SupportString;
import com.example.shushi.testfirebase.ChangePassWordActivity;
import com.example.shushi.testfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class AccountKitActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "le8pHIno3DEcNuNMPOrhzDBQ8";
    private static final String TWITTER_SECRET = "hhYS8jYFwTPzvNoOLgmEZnYvmTdoXQsyi6PmrUQhNDYaT6ztLA";
    public static int APP_REQUEST_CODE = 99;
    private Button btnlogout;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);


        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        setContentView(R.layout.activity_account_kit);
        auth = FirebaseAuth.getInstance();
        btnlogout = (Button) findViewById(R.id.btnlogout_account_kit);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digits.clearActiveSession();
                Toast.makeText(getApplicationContext(), "Authentication succes for "
                        , Toast.LENGTH_LONG).show();
            }
        });
        //Code verification twitter

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model

                String passw=CreatePassword(6);

                auth.createUserWithEmailAndPassword(SupportString.addSuffixeString(phoneNumber),passw).addOnCompleteListener(AccountKitActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                });
                Intent intet = new Intent(getApplicationContext(), ChangePassWordActivity.class);
                intet.putExtra("number", phoneNumber);
                startActivity(intet);
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });


    }


    public String CreatePassword(int length)
    {
        String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder res = new StringBuilder();
        Random rnd = new Random();
        while (res.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * valid.length());
            res.append(valid.charAt(index));
        }
        String saltStr = res.toString();
        return saltStr;

    }
}
