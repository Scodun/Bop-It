package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.ResponseModel;
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
     * @return score from this game
     */
    int sendGameResult(String userId, boolean result, ResponseModel responseModel);

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
     * Client polls the current round result. This call is expected between sendGameResult and startNewGame
     * @return
     */
    User[] getRoundResult();

}
