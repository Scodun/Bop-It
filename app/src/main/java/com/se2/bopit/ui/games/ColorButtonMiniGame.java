package com.se2.bopit.ui.games;

import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.ui.ButtonMiniGameFragment;

import static com.se2.bopit.domain.ButtonColor.*;

public class ColorButtonMiniGame extends ButtonMiniGameFragment {
    public ColorButtonMiniGame() {
        super(new GameModel<>("Select RED !",
                new ButtonModel(RED, "Red"),
                new ButtonModel(GREEN, "Green"),
                new ButtonModel(BLUE, "Blue")));
    }
}
