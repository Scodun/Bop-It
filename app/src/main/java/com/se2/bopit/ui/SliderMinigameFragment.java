package com.se2.bopit.ui;

import android.widget.SeekBar;
import com.se2.bopit.domain.sliderminigame.SliderGameModel;
import com.se2.bopit.domain.sliderminigame.SliderResponseModel;

public class SliderMinigameFragment extends MiniGameFragment<SliderGameModel>{

    private static SliderGameModel sliderGameModel = new SliderGameModel();

    public SliderMinigameFragment(SliderGameModel sliderGameModel) {
        super(sliderGameModel);
    }

    public static SeekBar.OnSeekBarChangeListener getListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sliderGameModel.handleResponse(new SliderResponseModel(seekBar, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        };
    }

    public static void setSliderGameModel(SliderGameModel sliderGameModel) {
        SliderMinigameFragment.sliderGameModel = sliderGameModel;
    }
}
