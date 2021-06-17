package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.responsemodel.ResponseModel;
import com.se2.bopit.ui.DifficultyActivity;

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

    default long getTime(Difficulty difficulty, int score) {
        double maxExponent = 7;
        double multiplier = 0.08;

        int base;
        switch (DifficultyActivity.difficulty) {
            case EASY:
                base = 3000;
                break;
            case HARD:
                base = 1000;
                break;
            default:
                base = 2000;
                break;
        }

        return generateTime(maxExponent, multiplier, base, score);
    }

    default long generateTime(double maxExponent, double scoreMultiplier, int base, int score) {
        return (long) (Math.exp(maxExponent - score * scoreMultiplier) + base);
    }
}
