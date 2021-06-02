package com.se2.bopit.domain.mock;

import com.se2.bopit.domain.interfaces.GameListener;

public class GameListenerMock implements GameListener {

    public boolean result;

    @Override
    public void onGameResult(boolean result) {
        this.result = result;
    }
}
