package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonMiniGameModel;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.annotations.MiniGameType;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;

import static com.se2.bopit.domain.ButtonColor.*;

@MiniGameType(name = "Color Buttons")
public class ColorButtonMiniGame extends ButtonMiniGameFragment {

    static final ArrayList<ButtonModel> possibleAnswers = initializeButtonModels();

    static final int numberAnswers = 3;

    public ColorButtonMiniGame() {
        super(ButtonMiniGameModel.createRandomGameModel(possibleAnswers, numberAnswers));
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
