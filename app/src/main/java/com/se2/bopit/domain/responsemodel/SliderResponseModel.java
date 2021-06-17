package com.se2.bopit.domain.responsemodel;

import android.widget.SeekBar;

public class SliderResponseModel extends ResponseModel {

    public final SeekBar slider;
    public final int progress;

    public SliderResponseModel(SeekBar slider, int progress) {
        this.slider = slider;
        this.progress = progress;
    }
}
