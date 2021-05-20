package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonMiniGameModel;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.annotations.MiniGameType;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;

@MiniGameType(enableByDefault = false)
public class WeirdTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int numberAnswers = 3;

    public WeirdTextButtonMiniGame() {
        super(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, numberAnswers));
    }

    private static ArrayList<ButtonModel> initializeButtonModels() {
        String answer = "BOP";
        char first = answer.charAt(0);
        char last = answer.charAt(answer.length() - 1);
        String middle = answer.substring(1, answer.length() - 1);
        ArrayList<ButtonModel> buttonModelsTmp = new ArrayList<>();
        buttonModelsTmp.add(new ButtonModel(first + middle + first));
        buttonModelsTmp.add(new ButtonModel(last + middle + first));
        buttonModelsTmp.add(new ButtonModel(last + middle + last));
        return buttonModelsTmp;
    }
}
