package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonMiniGameModel;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;

public class SimpleTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int numberAnswers = 3;

    public SimpleTextButtonMiniGame() {
        super(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, numberAnswers));
    }

    private static ArrayList<ButtonModel> initializeButtonModels() {
        ArrayList<ButtonModel> buttonModelsTmp = new ArrayList<>();
        buttonModelsTmp.add(new ButtonModel("BOP"));
        buttonModelsTmp.add(new ButtonModel("NOP"));
        buttonModelsTmp.add(new ButtonModel("OOP"));
        buttonModelsTmp.add(new ButtonModel("HOP"));
        buttonModelsTmp.add(new ButtonModel("MOP"));
        return buttonModelsTmp;
    }
}
