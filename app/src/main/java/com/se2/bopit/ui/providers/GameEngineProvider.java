package com.se2.bopit.ui.providers;

import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;

public class GameEngineProvider {
    static GameEngineProvider instance;
    public static GameEngineProvider getInstance() {
        if(instance == null) {
            instance = new GameEngineProvider();
        }
        return instance;
    }

    public GameEngine create(GameMode gameMode, GameEngineListener gameListener) {
        return create(gameMode,
                MiniGamesRegistry.getInstance(),
                new AndroidPlatformFeaturesProvider(),
                gameListener);
    }

    public GameEngine create(GameMode gameMode,
                                    MiniGamesProvider gamesProvider,
                                    PlatformFeaturesProvider platformFeaturesProvider,
                                    GameEngineListener gameListener) {
        return new GameEngine(gamesProvider, platformFeaturesProvider, gameListener);
    }
}
