package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.se2.bopit.MainActivity;
import com.se2.bopit.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int i = 0;
    private TextView textLoad;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        textLoad = findViewById(R.id.textViewLoading);
        textLoad.setText("");

        final int period = 100;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textLoad.setText("Loading " + i + " %");
                        }
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