package com.se2.bopit.domain.interfaces;

public interface MiniGame {
    /**
     * Method to set Listener in engine to listen for Minigame Events
     * @param listener - Game Listener
     */
    void setGameListener(GameListener listener);
}
