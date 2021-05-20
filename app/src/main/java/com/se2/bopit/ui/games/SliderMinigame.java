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
import androidx.fragment.app.Fragment;
import com.se2.bopit.R;
import com.se2.bopit.domain.SliderListener;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.Random;

public class SliderMinigame extends Fragment implements MiniGame {

    GameListener gameListener;

    SeekBar slider;
    int target;

    Random random = new Random();

    @Override
    public void setGameListener(GameListener listener) {
        gameListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);
        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText("Slide");

        View slidersFragment = inflater.inflate(R.layout.fragment_slider, container, false);
        slider = slidersFragment.findViewById(R.id.slider1);

        setupSlider();

        layout.addView(slidersFragment);

        return view;
    }

    private void setupSlider() {

        target = random.nextInt(9) + 1;

        int progress = target;
        while (progress == target || progress == 1)
            progress = random.nextInt(9) + 1;
        slider.setProgress(progress);

        slider.setOnSeekBarChangeListener(new SliderListener(this));

    }

    public void sliderStatus(int progress) {
        if (progress == target)
            gameListener.onGameResult(true);
    }

}
