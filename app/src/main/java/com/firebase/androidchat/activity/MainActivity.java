package com.firebase.androidchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.firebase.androidchat.R;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        boolean login = prefs.getBoolean("login", false);
        final Intent intent = new Intent();
        intent.setClass(this, login ? ChannelActivity.class : LoginActivity.class);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
