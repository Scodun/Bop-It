package com.se2.bopit.domain.sliderminigame;

import android.widget.SeekBar;
import com.se2.bopit.domain.ResponseModel;

public class SliderResponseModel extends ResponseModel {

    public final SeekBar slider;
    public final int progress;

    public SliderResponseModel(SeekBar slider, int progress) {
        this.slider = slider;
        this.progress = progress;
    }
}
