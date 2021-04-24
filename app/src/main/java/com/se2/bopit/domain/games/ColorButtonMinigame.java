package com.se2.bopit.domain.games;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameListener;
import com.se2.bopit.domain.MiniGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ColorButtonMinigame extends Fragment implements MiniGame {

    private GameListener listener;
    private Button btn1,btn2,btn3;
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
        btn1 = (Button)getView().findViewById(R.id.button_1);
        btn2 = (Button)getView().findViewById(R.id.button_2);
        btn3 = (Button)getView().findViewById(R.id.button_3);


        btn1.setOnClickListener(handleClick);
        btn2.setOnClickListener(handleClick);
        btn3.setOnClickListener(handleClick);
    }


    private final View.OnClickListener handleClick = new View.OnClickListener(){
        public void onClick(View view){
            listener.onGameResult(view == btn1);
        }
    };


}
