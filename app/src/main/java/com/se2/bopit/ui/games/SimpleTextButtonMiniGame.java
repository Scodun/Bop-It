package com.se2.bopit.ui.games;

import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;

public class SimpleTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int NUMBER_ANSWERS = 3;

    public SimpleTextButtonMiniGame() {
        this(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, NUMBER_ANSWERS));
    }

    public SimpleTextButtonMiniGame(ButtonMiniGameModel gameModel) {
        super(gameModel);
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

    @Override
    public long getTime(Difficulty difficulty, int score) {
        double maxExponent = 6.9;
        double multiplier = 0.07;

        int base;
        switch (DifficultyActivity.difficulty) {
            case EASY:
                base = 1600;
                break;
            case HARD:
                base = 800;
                break;
            default:
                base = 1200;
        }

        return generateTime(maxExponent, multiplier, base, score);
    }
}
