package com.se2.bopit.domain;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.domain.games.ColorButtonMinigame;

public class GameEngine {
    private CountDownTimer timer;
    private MiniGame minigame;
    private GameEngineListener listener;
    private int score = 0;

    public GameEngine() {
        listener = null;
    }

    public void startNewGame() {
        minigame = new ColorButtonMinigame();
        long time = (long) (Math.exp(-this.score*0.08+7)+1000);
        timer = startCountDown(time);
        if(this.listener != null){
            listener.onGameStart(minigame, time);
        }
        minigame.setGameListener(onGameResult);
    }

    private CountDownTimer startCountDown(long time){
        return new CountDownTimer(time, 5) {
            public void onTick(long millisUntilFinished) {
                if(listener != null)
                    listener.onTimeTick(millisUntilFinished);
            }

            public void onFinish() {
                if(listener != null)
                    listener.onTimeEnd();
            }
        }.start();
    }

    private final GameListener onGameResult = new GameListener() {
        @Override
        public void onGameResult(boolean result) {
            timer.cancel();
            if(listener != null) {
                if(result) {
                    score++;
                    listener.onScoreUpdate(score);
                    startNewGame();
                }
                else {
                    listener.onGameEnd(score);
                }
            }

        }
    };

    public void setGameEngineListener(GameEngineListener listener){
        if(listener != null)
            this.listener = listener;
    }

}
