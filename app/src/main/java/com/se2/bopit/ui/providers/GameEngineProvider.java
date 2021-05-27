package com.se2.bopit.ui.providers;

import com.se2.bopit.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;

import java.util.Collections;

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
        switch (gameMode) {
            case SINGLE_PLAYER:
                return createSinglePlayer(gamesProvider, platformFeaturesProvider, gameListener);
            default:
                throw new RuntimeException("Not implemented!");
        }

    }

    GameEngine createSinglePlayer(MiniGamesProvider gamesProvider,
                                  PlatformFeaturesProvider platformFeaturesProvider,
                                  GameEngineListener gameListener) {
        String singleUserId = "Player";
        GameEngine client = new GameEngine(gamesProvider, platformFeaturesProvider, gameListener);
        client.userId = singleUserId;
        GameEngineServer server = new GameEngineServer(gamesProvider, platformFeaturesProvider,
                Collections.singletonMap(singleUserId, new User(singleUserId, singleUserId)));
        SinglePlayerGameEngineDataProvider dataProvider = new SinglePlayerGameEngineDataProvider(client, server);
        client.dataProvider = dataProvider;
        server.dataProvider = dataProvider;
        return client;
    }
}
