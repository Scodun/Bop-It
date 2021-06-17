package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.GameRoundModel;

public interface MiniGamesProvider {
    MiniGame createRandomMiniGame();

    MiniGame createMiniGame(GameRoundModel round);
}
