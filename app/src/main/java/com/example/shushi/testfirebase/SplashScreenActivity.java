package com.example.shushi.testfirebase;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private static final long SPLASH_TIME=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=(ProgressBar)findViewById(R.id.progressBarSplashScreen);
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent=new Intent().setClass(SplashScreenActivity.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,SPLASH_TIME);
    }
}
