package com.se2.bopit.domain;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameEngine {
    GameEngineListener listener;

    int score = 0;
    boolean isOverTime = false;
    boolean miniGameLost = false;
    private Random rand;

    private ArrayList<Class<?>> miniGames;
    MiniGamesProvider miniGamesProvider;
    PlatformFeaturesProvider platformFeaturesProvider;

    @Deprecated
    public GameEngine() {
        listener = null;

        //Add MiniGame Classes here like ColorButtonMinigame.class
        miniGames = new ArrayList<>(
                Arrays.asList(
                        ColorButtonMiniGame.class,
                        SimpleTextButtonMiniGame.class,
                        WeirdTextButtonMiniGame.class,
                        ImageButtonMinigame.class
                )
        );
    }

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
        CountDownTimer timer = startCountDown(time);
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
                else {
                    miniGameLost = true;
                    listener.onGameEnd(score);
                }
            }

        });
    }

        private MiniGame getMiniGame(){
        if(miniGamesProvider != null)
            return miniGamesProvider.createRandomMiniGame();
        rand = new Random();
        try {
             return (MiniGame) miniGames.get(rand.nextInt(miniGames.size())).getDeclaredConstructor().newInstance();
        }catch(Exception e){
            Log.e("GameEngine", "Fatal Error creating Instance of Minigame, check if GameArray is correct!");
        }
        //Use Basic game if creating Instance randomly fails
        return new ColorButtonMiniGame();
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


    /**
     * @param listener - Listener to add to the Engine
     * Adds a Listener to the engine
     */
    public void setGameEngineListener(GameEngineListener listener){
        if(listener != null)
            this.listener = listener;
    }

}
