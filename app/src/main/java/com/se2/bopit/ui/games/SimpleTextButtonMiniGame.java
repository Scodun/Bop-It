package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleTextButtonMiniGame extends ButtonMiniGameFragment {

    private static final ArrayList<ButtonModel> buttonModels = initializeButtonModels();

    private static int numberButtons = 3;

    public SimpleTextButtonMiniGame() {
        super(createGameModel());
    }

    private static GameModel<ButtonModel> createGameModel() {
        Collections.shuffle(buttonModels);

        ArrayList<ButtonModel> wrongResponses = new ArrayList<>();
        for (int i=1; i<numberButtons; i++) {
            wrongResponses.add(buttonModels.get(i));
        }

        return new GameModel<>(
                String.format("Select %s!", buttonModels.get(0).label),
                buttonModels.get(0),
                wrongResponses
        );
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
