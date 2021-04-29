package com.se2.bopit.ui.games;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageButtonMinigame extends Fragment implements MiniGame {

    GameListener listener;

    ImageButton one;
    ImageButton two;
    ImageButton three;

    public ImageButtonMinigame(){
        super(R.layout.fragment_image_button_game);
        listener = null;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.imageButton).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton2).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton3).setOnClickListener(clickHandler);

        LinearLayout layout = getView().findViewById(R.id.linearLayout);

        one = getView().findViewById(R.id.imageButton);
        two = getView().findViewById(R.id.imageButton2);
        three = getView().findViewById(R.id.imageButton3);

        List<ImageButton> imageButtonList = new ArrayList<>();
        imageButtonList.add(one);
        imageButtonList.add(two);
        imageButtonList.add(three);

        Collections.shuffle(imageButtonList);

        layout.removeView(getView().findViewById(R.id.imageButton));
        layout.removeView(getView().findViewById(R.id.imageButton2));
        layout.removeView(getView().findViewById(R.id.imageButton3));

        layout.addView(imageButtonList.get(0));
        layout.addView(imageButtonList.get(1));
        layout.addView(imageButtonList.get(2));
    }

    private final View.OnClickListener clickHandler = new View.OnClickListener() {
        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onClick(View v) {
            listener.onGameResult(v.getId() == R.id.imageButton);
            one.setBackground(one.getResources().getDrawable(R.drawable.pressed_button_true));
        }
    };
}
