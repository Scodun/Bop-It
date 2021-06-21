package com.se2.bopit.ui.games;

import com.github.javafaker.Faker;
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;

import static com.se2.bopit.domain.Difficulty.EASY;
import static com.se2.bopit.domain.Difficulty.HARD;

public class SpecialTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static final int NUMBER_ANSWERS = 3;

    public SpecialTextButtonMiniGame() {
        this(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, NUMBER_ANSWERS));
    }

    public SpecialTextButtonMiniGame(ButtonMiniGameModel gameModel) {
        super(gameModel);
    }

    private static ArrayList<ButtonModel> initializeButtonModels() {
        ArrayList<ButtonModel> buttonModelsTmp = new ArrayList<>();
        Faker faker = new Faker();
        for(int i = 0; i<20; i++)
            buttonModelsTmp.add(new ButtonModel(String.valueOf(faker.number().numberBetween(0,200))));
        return buttonModelsTmp;
    }

    @Override
    public long getTime(Difficulty difficulty, int score) {
        if(DifficultyActivity.difficulty == EASY)
            return generateTime(6.9, 0.07, 1601, score);
        else if(DifficultyActivity.difficulty == HARD)
            return generateTime(6.9, 0.07, 801, score);
        else
            return generateTime(6.9, 0.07, 1201, score);
    }
}
