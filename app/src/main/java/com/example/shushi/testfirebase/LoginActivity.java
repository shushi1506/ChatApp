package com.example.shushi.testfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shushi.account_kit.AccountKitActivity;
import com.example.shushi.supports.SupportString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.Sinch;

import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;

public class LoginActivity extends BaseActivity implements View.OnClickListener, SinchService.StartFailedListener {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        try {
            if (auth.getCurrentUser() != null) {
                startActivity(new Intent(this, TabControlActivity.class));
                finish();
                try {
//                    loginClicked("anh");
                    if (!getSinchServiceInterface().isStarted()) {
                    }
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnSignup) {
            // startActivity(new Intent(LoginActivity.this, AccountKitActivity.class));
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        }
        if (v == btnReset) {
            startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        }
        if (v == btnLogin) {
            String email = inputEmail.getText().toString();
            final String call;
            final String password = inputPassword.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String email1 = SupportString.addSuffixeString(email).trim();
                email = email1;
                call = inputEmail.getText().toString();
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter PassWord", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            //auth user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        if (password.length() < 6) {
                            inputPassword.setError(getString(R.string.minimum_password));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loginClicked(call);
                        Intent intent = new Intent(LoginActivity.this, TabControlActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        //openPlaceCallActivity();
    }

    private void loginClicked(String email) {


        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            try {
                getSinchServiceInterface().startClient(email);

                showSpinner();
            } catch (Exception ex) {
            }
        } else {
            try {
                //  openPlaceCallActivity();
            } catch (Exception ex) {
            }
        }

    }

    private void stopClient() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}
