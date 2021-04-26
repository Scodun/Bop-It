package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

public class TextButtonMiniGame extends ButtonMiniGameFragment {
    public TextButtonMiniGame() {
        super(new GameModel<>("Click BOP !",
                new ButtonModel("BOP"),
                new ButtonModel("NOPE"),
                new ButtonModel("OOP"),
                new ButtonModel("HOP")));
    }
}
