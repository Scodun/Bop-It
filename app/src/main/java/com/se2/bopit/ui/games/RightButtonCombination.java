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

    boolean result = false;
    boolean result2 = false;

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
            TextView textView = getView().findViewById(R.id.textView2);
            String textFromTextView = textView.getText().toString();

            if(textFromTextView.equals("RIGHT, RIGHT")){
                result = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressRight;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, LEFT")){
                result = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressLeft;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, UP")){
                result = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressUp;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("RIGHT, DOWN")){
                result = v.getId() == R.id.pressRight;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressDown;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, RIGHT")){
                result = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressRight;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, LEFT")){
                result = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressLeft;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, UP")){
                result = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressUp;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("LEFT, DOWN")){
                result = v.getId() == R.id.pressLeft;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressDown;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("UP, RIGHT")){
                result = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressRight;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("UP, LEFT")){
                result = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressLeft;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("UP, UP")){
                result = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressUp;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("UP, DOWN")){
                result = v.getId() == R.id.pressUp;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressDown;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, RIGHT")){
                result = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressRight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressRight;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, LEFT")){
                result = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressLeft).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressLeft;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, UP")){
                result = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressUp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressUp;
                        listener.onGameResult(result,result2);
                    }
                });
            }
            if(textFromTextView.equals("DOWN, DOWN")){
                result = v.getId() == R.id.pressDown;
                getView().findViewById(R.id.pressDown).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        result2 = v.getId() == R.id.pressDown;
                        listener.onGameResult(result,result2);
                    }
                });
            }
        }
    };
}
