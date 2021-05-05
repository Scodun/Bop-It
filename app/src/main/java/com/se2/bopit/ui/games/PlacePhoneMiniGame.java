package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ActionModel;
import com.se2.bopit.ui.ActionMiniGameFragment;

import java.util.ArrayList;

public class PlacePhoneMiniGame extends ActionMiniGameFragment {

    private static final ArrayList<ActionModel> possibleAnswers = initializeActionModels();

    private static int numberAnswers = 2;

    public PlacePhoneMiniGame() {
        super(createGameModel(possibleAnswers, numberAnswers));
    }

    private static ArrayList<ActionModel> initializeActionModels() {
        ArrayList<ActionModel> ActionModelsTmp = new ArrayList<>();
        ActionModelsTmp.add(new ActionModel("facedown"));
        ActionModelsTmp.add(new ActionModel("faceup"));
        return ActionModelsTmp;
    }
}