package com.se2.bopit.ui.providers;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.se2.bopit.domain.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.data.DataProviderContext;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;

import java.util.Collections;
import java.util.stream.Collectors;

public class GameEngineProvider {
    static final String TAG = GameEngineProvider.class.getSimpleName();

    static GameEngineProvider instance;
    public static GameEngineProvider getInstance() {
        if(instance == null) {
            instance = new GameEngineProvider();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GameEngine create(GameMode gameMode, GameEngineListener gameListener) {
        Log.d(TAG, "create game engine for " + gameMode);
        return create(gameMode,
                MiniGamesRegistry.getInstance(),
                new AndroidPlatformFeaturesProvider(),
                gameListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        return new GameEngine(gamesProvider, platformFeaturesProvider, gameListener, context.getDataProvider());
    }

    GameEngine createSinglePlayer(MiniGamesProvider gamesProvider,
                                  PlatformFeaturesProvider platformFeaturesProvider,
                                  GameEngineListener gameListener) {
        SinglePlayerGameEngineDataProvider dataProvider = new SinglePlayerGameEngineDataProvider();
        GameEngine client = new GameEngine(gamesProvider, platformFeaturesProvider, gameListener, dataProvider);
        String singleUserId = dataProvider.getUserId();
        GameEngineServer server = new GameEngineServer(gamesProvider, platformFeaturesProvider,
                Collections.singletonMap(singleUserId, new User(singleUserId, singleUserId)), dataProvider);
        return client;
    }
}
