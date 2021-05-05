package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class RightButtonCombination extends Fragment implements MiniGame {

    GameListener listener;

    RightButtonCombination(){
        super(R.layout.fragment_right_button_combination_game);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getView().findViewById(R.id.pressDown).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressRight).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressLeft).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressUp).setOnClickListener(clickHandler);

    }

    public final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
