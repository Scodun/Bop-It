package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import java.util.ArrayList;
import java.util.List;

public class WeirdTextButtonMiniGame extends ButtonMiniGameFragment {

    public WeirdTextButtonMiniGame() {
        super(createGameModel("BOP"));
    }

    static GameModel<ButtonModel> createGameModel(String answer) {
        char first = answer.charAt(0);
        char last = answer.charAt(answer.length() - 1);
        String middle = answer.substring(1, answer.length() - 1);
        return new GameModel<>("Hit " + answer + "!",
                new ButtonModel(answer),
                new ButtonModel(first + middle + first),
                new ButtonModel(last + middle + first),
                new ButtonModel(last + middle + last) );
    }
}
