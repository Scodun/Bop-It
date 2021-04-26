package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

public class ColorButtonMiniGame extends ButtonMiniGameFragment {
    public ColorButtonMiniGame() {
        super(new GameModel<>("Select RED !",
                new ButtonModel(0xFF000, "Red"),
                new ButtonModel(0x00FF00, "Green"),
                new ButtonModel(0x000FF, "Blue")));
    }
}
