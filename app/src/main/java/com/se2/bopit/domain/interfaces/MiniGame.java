package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.responsemodel.ResponseModel;
import com.se2.bopit.ui.DifficultyActivity;

import static com.se2.bopit.domain.Difficulty.EASY;
import static com.se2.bopit.domain.Difficulty.HARD;

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
        if (DifficultyActivity.getDifficulty() == EASY)
            return generateTime(7, 0.08, 3000, score);
        else if (DifficultyActivity.getDifficulty() == HARD)
            return generateTime(6.9, 0.07, 1000, score);
        else
            return generateTime(6.9, 0.07, 2000, score);
    }

    default long generateTime(double maxExponent, double scoreMultiplier, int base, int score) {
        return (long) (Math.exp(maxExponent - score * scoreMultiplier) + base);
    }
}
