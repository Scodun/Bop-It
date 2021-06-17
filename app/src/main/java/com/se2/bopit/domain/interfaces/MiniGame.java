package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.responsemodel.ResponseModel;

public interface MiniGame {
    /**
     * Method to set Listener in engine to listen for Minigame Events
     *
     * @param listener - Game Listener
     */
    void setGameListener(GameListener listener);

    /**
     * Propagates PlatformFeaturesProvider from GameEngine
     *
     * @param provider an instance of PlatformFeaturesProvider
     */
    default void setPlatformFeaturesProvider(PlatformFeaturesProvider provider) {
        // do nothing. override in games that use platform features!
    }

    GameModel<? extends ResponseModel> getModel();
}
