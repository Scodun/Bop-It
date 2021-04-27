package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;
import java.util.Collections;

import static com.se2.bopit.domain.ButtonColor.*;

public class ColorButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> buttonModels = initializeButtonModels();

    private static int numberButtons = 3;

    public ColorButtonMiniGame() {
        super(createGameModel());
    }

    private static GameModel<ButtonModel> createGameModel() {
        Collections.shuffle(buttonModels);

        ButtonModel correctResponse = buttonModels.get(0);

        ArrayList<ButtonModel> wrongResponses = new ArrayList<>();
        for (int i=1; i<numberButtons; i++) {
            ButtonModel wrongResponse = buttonModels.get(i);
            wrongResponse.isCorrect = false;
            wrongResponses.add(wrongResponse);
        }

        return new GameModel<>(
                String.format("Select %s!", correctResponse.label),
                correctResponse,
                wrongResponses
        );
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
