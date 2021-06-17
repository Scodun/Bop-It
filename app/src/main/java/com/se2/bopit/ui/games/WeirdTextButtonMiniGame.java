package com.se2.bopit.ui.games;

import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.annotations.MiniGameType;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;

@MiniGameType(enableByDefault = false)
public class WeirdTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int numberAnswers = 3;

    public WeirdTextButtonMiniGame() {
        this(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, numberAnswers));
    }

    public WeirdTextButtonMiniGame(ButtonMiniGameModel gameModel) {
        super(gameModel);
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
                break;
        }

        return generateTime(maxExponent, multiplier, base, score);
    }
}
