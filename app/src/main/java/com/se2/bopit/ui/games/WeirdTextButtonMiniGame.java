package com.se2.bopit.ui.games;

import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.annotations.MiniGameType;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;

import static com.se2.bopit.domain.Difficulty.EASY;
import static com.se2.bopit.domain.Difficulty.HARD;

@MiniGameType(enableByDefault = false)
public class WeirdTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int NUMBER_ANSWERS = 3;

    public WeirdTextButtonMiniGame() {
        this(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, NUMBER_ANSWERS));
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
        if (DifficultyActivity.getDifficulty() == EASY)
            return generateTime(6.9, 0.07, 1600, score);
        else if (DifficultyActivity.getDifficulty() == HARD)
            return generateTime(6.9, 0.07, 800, score);
        else
            return generateTime(6.9, 0.07, 1200, score);
    }
}
