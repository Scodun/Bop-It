package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.se2.bopit.domain.services.BackgroundSoundService;

public class BaseActivity extends AppCompatActivity {
    private static Intent music;
    private static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.isRunning = true;
        if (BaseActivity.music == null) {
            BaseActivity.music = new Intent(this, BackgroundSoundService.class);
            startService(BaseActivity.music);
            BaseActivity.isRunning = true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop mediaplayer:
        BaseActivity.isRunning = false;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (!BaseActivity.isRunning)
                            stopService(BaseActivity.music);
                    }
                },
                1000
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BaseActivity.isRunning) {
            startService(BaseActivity.music);
            BaseActivity.isRunning = true;
        }
    }
}
