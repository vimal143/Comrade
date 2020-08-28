package com.vimal.commrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Splash_Screen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4;
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.checkLogin()) {
            Runnable runnable = new Runnable() {
                public void run() {
                    Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            worker.schedule(runnable, SPLASH_SCREEN, TimeUnit.SECONDS);
        } else {
            Runnable runnable = new Runnable() {
                public void run() {
                    Intent intent = new Intent(Splash_Screen.this, Signup.class);
                    startActivity(intent);
                    finish();
                }
            };
            worker.schedule(runnable, SPLASH_SCREEN, TimeUnit.SECONDS);
        }
    }


}