package com.se2.bopit.domain;

import android.graphics.PorterDuff;
import android.widget.SeekBar;
import com.se2.bopit.R;
import com.se2.bopit.ui.games.SliderMinigame;

public class SliderListener implements SeekBar.OnSeekBarChangeListener {

    private final int target;
    private SliderMinigame minigame;

    public SliderListener(int target, SliderMinigame minigame) {
        this.target = target;
        this.minigame = minigame;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        System.out.println(progress);
        if (progress == target) {
            seekBar.getThumb().setColorFilter(seekBar.getResources().getColor(R.color.green), PorterDuff.Mode.MULTIPLY);
            seekBar.setBackgroundColor(seekBar.getResources().getColor(R.color.green));
            minigame.sliderStatus(seekBar, true);
        } else {
            minigame.sliderStatus(seekBar, false);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("start");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("stop");
    }
}
