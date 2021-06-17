package com.se2.bopit.domain.data;

import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;

public abstract class DataProviderStrategy implements GameEngineDataProvider {

    protected GameEngine gameEngineClient;
    protected GameEngineServer gameEngineServer;

    public abstract void connectToEndpoint(String endpointId);

    public abstract void startDiscovery();

    public abstract void startAdvertising();

    public abstract void setListener(NetworkLobbyListener listener);

    public abstract void setListener(NetworkContextListener listener);

    public abstract void startGameCountdown();

    public abstract void disconnect();

    public abstract void sendReadyMessage();

    public abstract void sendReadyAnswer(boolean answer, String username);

    @Override
    public void setGameEngineClient(GameEngine client) {
        this.gameEngineClient = client;
    }

    @Override
    public void setGameEngineServer(GameEngineServer server) {
        this.gameEngineServer = server;
    }
}
