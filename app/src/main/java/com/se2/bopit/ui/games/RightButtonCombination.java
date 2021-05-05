package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RightButtonCombination extends Fragment implements MiniGame {

    GameListener listener;

    boolean result;
    boolean result2;

    RightButtonCombination(){
        super(R.layout.fragment_right_button_combination_game);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
        this.listener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.pressDown).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressRight).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressLeft).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressUp).setOnClickListener(clickHandler);

        String n = "";

        List<String> partOne = new ArrayList<>();
        partOne.add("RIGHT");
        partOne.add("LEFT");
        partOne.add("DOWN");
        partOne.add("UP");
        List<String> partTwo = new ArrayList<>();
        partOne.add("RIGHT");
        partOne.add("LEFT");
        partOne.add("DOWN");
        partOne.add("UP");

        Collections.shuffle(partOne);
        Collections.shuffle(partTwo);

        n = partOne.get(1)+" "+partTwo.get(0);

        TextView textView = getView().findViewById(R.id.textView2);
        textView.setText(n);

    }

    public final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
