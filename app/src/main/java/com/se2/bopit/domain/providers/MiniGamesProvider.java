package com.se2.bopit.domain.providers;

import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.interfaces.MiniGame;

public interface MiniGamesProvider {
    MiniGame createRandomMiniGame();

    MiniGame createMiniGame(GameRoundModel round);
}
