package com.se2.bopit.domain.gamemodel;

import android.widget.SeekBar;

import com.se2.bopit.domain.responsemodel.SliderResponseModel;
import com.se2.bopit.ui.SliderMinigameFragment;

import java.util.Random;

public class SliderGameModel extends GameModel<SliderResponseModel> {

    private static final Random random = new Random();
    public final int target = generateTarget();

    @Override
    public boolean checkResponse(SliderResponseModel response) {
        return response.progress == target;
    }

    @Override
    public boolean handleResponse(Object response) {
        boolean result = checkResponse((SliderResponseModel) response);
        if (result && listener != null)
            listener.onGameResult(true);
        return result;
    }

    public static void setupSlider(SeekBar slider) {
        slider.setOnSeekBarChangeListener(SliderMinigameFragment.getListener());
        slider.setProgress(generateProgress());
    }

    public static int generateProgress() {
        return random.nextInt(3) + 7;
    }

    public static int generateTarget() {
        // target is one of 2 3 4 5 6 10 11 12 13 14
        int target = random.nextInt(5) + 2;
        if (random.nextInt(2) == 0)
            target += 8;

        return target;
    }
}
