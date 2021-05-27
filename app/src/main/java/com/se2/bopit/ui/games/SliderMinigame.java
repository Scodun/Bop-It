package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.se2.bopit.R;
import com.se2.bopit.domain.sliderminigame.SliderGameModel;
import com.se2.bopit.ui.SliderMinigameFragment;

public class SliderMinigame extends SliderMinigameFragment {

    static SliderGameModel gameModel = new SliderGameModel();

    public SliderMinigame() {
        super(gameModel);
        SliderMinigameFragment.setSliderGameModel(gameModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);
        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText("Slide");

        View slidersFragment = inflater.inflate(R.layout.fragment_slider, container, false);
        SeekBar slider = slidersFragment.findViewById(R.id.slider1);

        SliderGameModel.setupSlider(slider);

        layout.addView(slidersFragment);

        return view;
    }

}
