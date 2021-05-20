package com.se2.bopit.domain;

import android.widget.SeekBar;
import com.se2.bopit.ui.games.SliderMinigame;

public class SliderListener implements SeekBar.OnSeekBarChangeListener {

    private final SliderMinigame minigame;

    public SliderListener(SliderMinigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        minigame.sliderStatus(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
