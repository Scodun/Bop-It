package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;


public class GameEngine {
    GameEngineListener listener;


    private int score = 0;
    private boolean isOverTime = false;
    private boolean miniGameLost = false;
    private boolean lifecycleCancel = false;
    private Random rand;
    private CountDownTimer timer;

    MiniGamesProvider miniGamesProvider;
    PlatformFeaturesProvider platformFeaturesProvider;

    public GameEngine(MiniGamesProvider miniGamesProvider,
                      PlatformFeaturesProvider platformFeaturesProvider,
                      GameEngineListener listener) {
        this.miniGamesProvider = miniGamesProvider;
        this.platformFeaturesProvider = platformFeaturesProvider;
        this.listener = listener;
    }

    /**
     * Starts a new Minigame
     * Initialises the Time e^(-score*0.08+7)+1000
     * This time will be used for the countdown:
     * Game 1   Time: 2096 ms
     * Game 10  Time: 1500 ms
     * Game 20  Time: 1221 ms
     * Game 30  Time: 1100 ms
     * Game 50  Time: 1020 ms
     * Game 100 Time: 1000 ms
     *
     * Calls the MainActivity onGameStart Listener to display the Fragment
     * Sets the GameListener for the Minigame
     */
    public void startNewGame() {
        MiniGame minigame = getMiniGame();
        long time = (long) (Math.exp(-this.score*0.08+7)+1000);
        timer = startCountDown(time);
        if(this.listener != null){
            listener.onGameStart(minigame, time);
        }

        minigame.setGameListener(result -> {
            timer.cancel();
            if(listener != null) {
                if(result && !isOverTime && !miniGameLost) {
                    score++;
                    listener.onScoreUpdate(score);
                    startNewGame();
                }
                else if(!lifecycleCancel){
                    miniGameLost = true;
                    listener.onGameEnd(score);
                }
            }

        });
    }

        private MiniGame getMiniGame(){
            return miniGamesProvider.createRandomMiniGame();
    }

    /**
     * @param time - countdown time in ms
     * Starts a new countdown
     * Calls the MainActivity onTimeTick, onFinish listener to display the time
     */
    private CountDownTimer startCountDown(long time) {
        return platformFeaturesProvider.createCountDownTimer(
                time, 5, this::onTick, this::onFinish)
                .start();
    }

            public void onTick(long millisUntilFinished) {
                if(listener != null)
                    listener.onTimeTick(millisUntilFinished);
            }

            public void onFinish() {
                isOverTime = true;
                if(listener != null)
                    listener.onGameEnd(score);
            }
        }.start();
    }


    /**
     * @param listener - Listener to add to the Engine
     * Adds a Listener to the engine
     */
    public void setGameEngineListener(GameEngineListener listener){
        if(listener != null)
            this.listener = listener;
    }

    public void stopCurrentGame(){
        if(!lifecycleCancel && !miniGameLost) {
            lifecycleCancel = true;
            timer.cancel();
            miniGameLost = true;
            listener.onGameEnd(score);
        }
    }

}
