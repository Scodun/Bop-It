package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.se2.bopit.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private int i = 0;
    private Timer timer;
    private ImageView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waveView=findViewById(R.id.waveView);
        final int period = 10;

        startLoadingAnimation(waveView);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100) {
                    i++;
                } else {
                    timer.cancel();
                    startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
                    finish();

                }
            }
        }, 0, period);
    }

    private void startLoadingAnimation(View view){
        Animation a = new Animation() {
            boolean isNextIteration=false;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(isNextIteration){
                    interpolatedTime=1-interpolatedTime;
                    isNextIteration=(interpolatedTime>0);
                }
                else {
                    isNextIteration = (interpolatedTime == 1);
                }
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) waveView.getLayoutParams();
                newLayoutParams.bottomMargin =(int)(100*interpolatedTime);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                newLayoutParams.width=(int)(2000*interpolatedTime+ metrics.widthPixels);
                newLayoutParams.height=(int)(interpolatedTime+ metrics.heightPixels*0.5);
                newLayoutParams.horizontalBias= interpolatedTime;
                waveView.setLayoutParams(newLayoutParams);
            }
        };
        a.setDuration(8000); // in ms
        a.setRepeatCount(Animation.INFINITE);

        view.startAnimation(a);
    }
}