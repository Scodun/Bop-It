package com.se2.bopit.ui.helpers;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WaveAnimator {

    private View waveView;
    private AppCompatActivity activity;

    public WaveAnimator(AppCompatActivity activity, View waveView) {
        this.waveView = waveView;
        this.activity = activity;
    }

    public void animate(double time, boolean isBottom) {
        Animation a = new Animation() {
            boolean isNextIteration = false;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (isNextIteration) {
                    interpolatedTime = 1 - interpolatedTime;
                    isNextIteration = (interpolatedTime > 0);
                } else {
                    isNextIteration = (interpolatedTime == 1);
                }
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) waveView.getLayoutParams();

                if (isBottom)
                    newLayoutParams.bottomMargin = (int) (100 * interpolatedTime);
                else
                    newLayoutParams.topMargin = (int) (100 * interpolatedTime);
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                newLayoutParams.width = (int) (1000 * interpolatedTime + metrics.widthPixels);
                newLayoutParams.horizontalBias = interpolatedTime;
                waveView.setLayoutParams(newLayoutParams);
            }
        };
        a.setDuration((long) (time)); // in ms
        a.setRepeatCount(Animation.INFINITE);

        waveView.startAnimation(a);
    }
}
