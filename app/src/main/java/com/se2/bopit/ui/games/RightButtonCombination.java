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

    boolean firstClick = false;
    boolean secondClick = false;
    boolean result = false;

    public RightButtonCombination(){


        super(R.layout.fragment_right_button_combination_game);
        this.listener = null;
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

        String n = "";

        List<String> partOne = new ArrayList<>();
        partOne.add("RIGHT");
        partOne.add("LEFT");
        partOne.add("DOWN");
        partOne.add("UP");
        List<String> partTwo = new ArrayList<>();
        partTwo.add("RIGHT");
        partTwo.add("LEFT");
        partTwo.add("DOWN");
        partTwo.add("UP");

        Collections.shuffle(partOne);
        Collections.shuffle(partTwo);

        n = partOne.get(0)+", "+partTwo.get(0);

        TextView textView = getView().findViewById(R.id.textView2);
        textView.setText(n);

    }

    public final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = getView().findViewById(R.id.textView2);
            String textFromTextView = textView.getText().toString();

            if(textFromTextView.equals("RIGHT, RIGHT")){
                firstClick = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressRight;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, LEFT")){
                firstClick = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressLeft;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, UP")){
                firstClick = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressUp;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, DOWN")){
                firstClick = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressDown;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, RIGHT")){
                firstClick = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressRight;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, LEFT")){
                firstClick = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressLeft;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, UP")){
                firstClick = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressUp;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, DOWN")){
                firstClick = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressDown;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("UP, RIGHT")){
                firstClick = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressRight;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("UP, LEFT")){
                firstClick = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressLeft;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("UP, UP")){
                firstClick = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressUp;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("UP, DOWN")){
                firstClick = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressDown;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, RIGHT")){
                firstClick = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressRight;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, LEFT")){
                firstClick = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressLeft;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, UP")){
                firstClick = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressUp;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, DOWN")){
                firstClick = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        secondClick = v.getId() == R.id.pressDown;
                        result = checkClick(firstClick,secondClick);
                        listener.onGameResult(result);
                    }
                });
            }
        }
    };
    public boolean checkClick(boolean click1, boolean click2){
        if(click1 && click2) {
            return true;
        }else{
            return false;
        }
    }
}
