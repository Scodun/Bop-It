package com.se2.bopit.domain.interfaces;

public interface GameListener {
    /**
     * Method called when game is finished
     * @param result - game result (true - won, false - lost)
     */
    void onGameResult(boolean result);
}
