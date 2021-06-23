package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.annotations.MiniGameType;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;

import static com.se2.bopit.domain.ButtonColor.DEFAULT;
import static com.se2.bopit.domain.ButtonColor.RANDOM;
import static com.se2.bopit.domain.Difficulty.EASY;
import static com.se2.bopit.domain.Difficulty.HARD;

@MiniGameType(name = "Color Buttons")
public class ColorButtonMiniGame extends ButtonMiniGameFragment {

    static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    static final int NUMBER_ANSWERS = 3;

    public ColorButtonMiniGame() {
        this(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, NUMBER_ANSWERS));
    }

    public ColorButtonMiniGame(ButtonMiniGameModel gameModel) {
        super(gameModel);
    }

    private static ArrayList<ButtonModel> initializeButtonModels() {
        ArrayList<ButtonModel> buttonModelsTmp = new ArrayList<>();
        for (ButtonColor buttonColor : ButtonColor.values()) {
            if (!buttonColor.equals(RANDOM) && !buttonColor.equals(DEFAULT)) {
                buttonModelsTmp.add(new ButtonModel(buttonColor));
            }
        }
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
