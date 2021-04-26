package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.se2.bopit.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int i = 0;
    private TextView textLoad;
    //private String loadingText;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        textLoad = findViewById(R.id.textViewLoading);


        final int period = 100;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100) {
                    runOnUiThread(() -> {
                        String loadingText = String.format(getResources().getString(R.string.loading), String.valueOf(i));
                        textLoad.setText(loadingText);
                    });
                    progressBar.setProgress(i);
                    i++;
                } else {
                    timer.cancel();
                    startActivity(new Intent(SplashActivity.this, GameActivity.class));
                    finish();

                }
            }
        }, 0, period);
    }
}