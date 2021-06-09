package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.ResponseModel;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.models.User;

public interface GameEngineDataProvider {

    /**
     * Client reports to server the readiness to start.
     * @param userId user ID
     */
    void readyToStart(String userId);

    /**
     * After all clients are ready, Server notifies all clients about new round.
     * @param roundModel game round model
     */
    void startNewGame(GameRoundModel roundModel);

    /**
     * Client sends Game result;
     * Server returns earned score;
     * @param result
     */
    void sendGameResult(String userId, boolean result, ResponseModel responseModel);

    /**
     * Server notifies all clients, but current, about current client response,
     * so that they can display it in UI.
     */
    void notifyGameResult(boolean result, ResponseModel responseModel);

    /**
     * Client stops current Game;
     * Server counts surrender if not lost yet and removes user from the list.
     * @param userId
     */
    void stopCurrentGame(String userId);

    /**
     * Server notofoes all clients about game over.
     */
    void notifyGameOver();

    /**
     * Client polls the current round result. This call is expected between sendGameResult and startNewGame
     * @return
     */
    User[] getRoundResult();

    void setGameEngineClient(GameEngine client);

    void setGameEngineServer(GameEngineServer server);

    String getUserId();

    /**
     * Client has cheated. Server set User hasCheated = true.
     */
    void setClientCheated();

    /**
     * Client detects cheating. Server stops Game for lastPlayer, if hasCheated = true.
     * Server take away one life from currentPlayer, if hasCheated = false.
     */
    void detectCheating();
}
