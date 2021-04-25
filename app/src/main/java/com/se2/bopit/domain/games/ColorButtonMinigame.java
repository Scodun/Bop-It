package com.se2.bopit.domain.games;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;


public class ColorButtonMinigame extends Fragment implements MiniGame {

    //Listener for Game specific events
    private GameListener listener;

    public ColorButtonMinigame() {
        super(R.layout.fragment_color_button_game);
        this.listener = null;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //add Click listener to all buttons
        //TODO: Use Button Component
        ((Button)getView().findViewById(R.id.button_1)).setOnClickListener(handleClick);
        ((Button)getView().findViewById(R.id.button_2)).setOnClickListener(handleClick);
        ((Button)getView().findViewById(R.id.button_3)).setOnClickListener(handleClick);
    }


    //Listener for Button click
    private final View.OnClickListener handleClick = new View.OnClickListener(){
        public void onClick(View view){
            listener.onGameResult(view.getId() == R.id.button_1);
        }
    };


}
