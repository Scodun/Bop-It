package com.se2.bopit.ui.games;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.RightButtonCombinationModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RightButtonCombination extends Fragment implements MiniGame {

    RightButtonCombinationModel rightButtonCombinationModel;

    String text;
    TextView textView;

    boolean firstClick = false;
    boolean secondClick = false;
    boolean result = false;

    int count;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RightButtonCombination(){
        super(R.layout.fragment_right_button_combination_game);
        rightButtonCombinationModel = RightButtonCombinationModel.createRandomModel();
    }

    @Override
    public void setGameListener(GameListener listener) {
        rightButtonCombinationModel.setGameListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeButtons();

        text = rightButtonCombinationModel.challenge;
        textView = getView().findViewById(R.id.textView2);
        textView.setText(text);

    }
    public void initializeButtons(){
        getView().findViewById(R.id.pressDown).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressRight).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressLeft).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressUp).setOnClickListener(clickHandler);
    }

    public final View.OnClickListener clickHandler = firstClickedButton -> {

        if(text.equals(rightButtonCombinationModel.challenge)){
            firstClick = firstClickedButton.getId() == findButton();
            count++;
            checkFirstClick();
        }

    };
    int findButton(){
        if(count == 0){
            switch(rightButtonCombinationModel.correctResponse.rightButton){
                case RIGHT:
                    return R.id.pressRight;
                case LEFT:
                    return R.id.pressLeft;
                case UP:
                    return R.id.pressUp;
                case DOWN:
                    return R.id.pressDown;
                default:
                    Log.e("RightButtonCombination", "Unknown Button");
                    return 0;
            }
        }else{
            switch(rightButtonCombinationModel.secondCorrectResponse.rightButton){
                case RIGHT:
                    return R.id.pressRight;
                case LEFT:
                    return R.id.pressLeft;
                case UP:
                    return R.id.pressUp;
                case DOWN:
                    return R.id.pressDown;
                default:
                    Log.e("RightButtonCombination", "Unknown Button");
                    return 0;
            }
        }
    }

    void checkFirstClick(){
        if(!firstClick){
            rightButtonCombinationModel.getGameListener().onGameResult(result);
        }else{
            setSecondOnClickListener();
        }
    }

    public boolean checkClick(boolean click1, boolean click2){
        return click1 && click2;
    }
    void setSecondOnClickListener(){
        getView().findViewById(findButton()).setOnClickListener(clickedButton -> {
            secondClick = clickedButton.getId() == findButton();
            result = checkClick(firstClick,secondClick);
            rightButtonCombinationModel.getGameListener().onGameResult(result);
        });
    }
}
