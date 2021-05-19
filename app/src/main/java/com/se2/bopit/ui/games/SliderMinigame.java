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

import java.util.ArrayList;
import java.util.HashMap;

public class SliderMinigame extends Fragment implements MiniGame {

    GameListener gameListener;
    SliderListener sliderListener ;
    ArrayList<SeekBar> sliders = new ArrayList<>();
    HashMap<SeekBar, Boolean> correct = new HashMap<>();

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
        sliders.add(slidersFragment.findViewById(R.id.slider1));
        sliders.add(slidersFragment.findViewById(R.id.slider2));

        sliderListener = new SliderListener(7, this);

        setupSliders();

        layout.addView(slidersFragment);

        return view;
    }

    private void setupSliders() {
        for (SeekBar slider : sliders) {
            slider.setOnSeekBarChangeListener(sliderListener);
            correct.put(slider, false);
        }
    }

    public void sliderStatus(SeekBar slider, boolean setTo) {
        System.out.println(slider.toString());
        correct.put(slider, setTo);
        checkEverythingCorrect();
    }

    public void checkEverythingCorrect() {

        for (SeekBar slider : correct.keySet()) {
            System.out.println("dd");
            if (!correct.get(slider))
                return;
        }
        gameListener.onGameResult(true);
    }
}
