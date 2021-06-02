package com.se2.bopit.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.ResponseModel;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.models.User;

public class SinglePlayerGameEngineDataProvider implements GameEngineDataProvider {

    static final String TAG = SinglePlayerGameEngineDataProvider.class.getSimpleName();

    GameEngine client;
    GameEngineServer server;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void readyToStart(String userId) {
        Log.d(TAG, "readyToStart: " + userId);
        server.readyToStart(userId);
    }

    @Override
    public void startNewGame(GameRoundModel roundModel) {
        Log.d(TAG, "startNewGame: " + roundModel);
        client.startNewGame(roundModel);
    }

    @Override
    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        Log.d(TAG, "sendGameResult: " + userId + ", " + result + ", " + responseModel);
        server.sendGameResult(userId, result, responseModel);
    }

    @Override
    public void notifyGameResult(boolean b, ResponseModel result) {
        // unused
    }

    @Override
    public void stopCurrentGame(String userId) {
        Log.d(TAG, "stopCurrentGame: " + userId);
        server.stopCurrentGame(userId);
    }

    @Override
    public User[] getRoundResult() {
        return server.getRoundResult();
    }

    @Override
    public void setGameEngineClient(GameEngine client) {
        this.client = client;
    }

    @Override
    public void setGameEngineServer(GameEngineServer server) {
        this.server = server;
    }

    @Override
    public void setClientCheated(String userId) {
        server.setClientCheated(userId);
    }

    @Override
    public void detectCheating(String userId) {
        server.detectCheating(userId);
    }

}
