package com.se2.bopit.ui.providers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.se2.bopit.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.data.DataProviderContext;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;

import java.util.Collections;
import java.util.stream.Collectors;

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
            case MULTI_PLAYER_SERVER:
                return createMultiPlayerHost(gamesProvider, platformFeaturesProvider, gameListener);
            case MULTI_PLAYER_CLIENT:
                return createMultiPlayerClient(gamesProvider, platformFeaturesProvider, gameListener);
            default:
                throw new RuntimeException("Not implemented!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    GameEngine createMultiPlayerHost(MiniGamesProvider gamesProvider, PlatformFeaturesProvider platformFeaturesProvider, GameEngineListener gameListener) {
        DataProviderContext context = DataProviderContext.getContext();
        GameEngine client = new GameEngine(gamesProvider, platformFeaturesProvider, gameListener, context.getDataProvider());
        client.userId = context.getUserId();
        GameEngineServer server = new GameEngineServer(gamesProvider, platformFeaturesProvider,
                context.getUsers().stream()
                        .collect(Collectors.toMap(User::getId, u -> u)),
                context.getDataProvider());
        return client;
    }

    GameEngine createMultiPlayerClient(MiniGamesProvider gamesProvider,
                                 PlatformFeaturesProvider platformFeaturesProvider,
                                 GameEngineListener gameListener) {
        DataProviderContext context = DataProviderContext.getContext();
        GameEngine client = new GameEngine(gamesProvider, platformFeaturesProvider, gameListener, context.getDataProvider());
        client.userId = context.getUserId();
        return client;
    }

    GameEngine createSinglePlayer(MiniGamesProvider gamesProvider,
                                  PlatformFeaturesProvider platformFeaturesProvider,
                                  GameEngineListener gameListener) {
        SinglePlayerGameEngineDataProvider dataProvider = new SinglePlayerGameEngineDataProvider();
        String singleUserId = "Player";
        GameEngine client = new GameEngine(gamesProvider, platformFeaturesProvider, gameListener, dataProvider);
        client.userId = singleUserId;
        GameEngineServer server = new GameEngineServer(gamesProvider, platformFeaturesProvider,
                Collections.singletonMap(singleUserId, new User(singleUserId, singleUserId)), dataProvider);
        return client;
    }
}
