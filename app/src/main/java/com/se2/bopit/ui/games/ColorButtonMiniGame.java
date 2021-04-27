package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;

import static com.se2.bopit.domain.ButtonColor.*;

public class ColorButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    private static int numberAnswers = 3;

    public ColorButtonMiniGame() {
        super(createGameModel(possibleAnswers, numberAnswers));
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
}
