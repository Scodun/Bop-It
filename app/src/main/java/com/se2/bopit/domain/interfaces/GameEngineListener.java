package com.se2.bopit.domain.interfaces;

public interface GameEngineListener {
    /**
     * Method called when game Ends (time end, miss click)
     *
     * @param score - Score at the end of the game
     */
    void onGameEnd(int score);

    /**
     * Method called when minigame won and score updated
     *
     * @param score - Score after minigame
     */
    void onScoreUpdate(int score);

    void onLifeUpdate(int life);

    /**
     * Method called when Minigame starts
     *
     * @param game - Minigame to start
     * @param time - Time (in ms) for the Minigame
     */
    void onGameStart(MiniGame game, long time);

    /**
     * Method called when Countdown Tick
     *
     * @param time - Time (in ms) left
     */
    void onTimeTick(long time);

}
