package com.se2.bopit.domain;

public interface GameEngineListener {
    void onGameEnd(int score);
    void onScoreUpdate(int score);
    void onGameStart(MiniGame game, long time);
    void onTimeTick(long time);
    void onTimeEnd();
}
