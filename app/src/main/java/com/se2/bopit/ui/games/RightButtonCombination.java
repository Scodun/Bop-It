package com.se2.bopit.ui.games;

import android.os.Build;
import android.os.Bundle;
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



    };
    public boolean checkClick(boolean click1, boolean click2){
        if(click1 && click2) {
            return true;
        }else{
            return false;
        }
    }
}
