package com.se2.bopit.domain.mock;

import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class MiniGameMock implements MiniGame {
    // public fields to enable manipulations from tests
    public GameListener listener;

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public GameModel<?> getModel() {
        return null;
    }

}
