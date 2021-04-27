package com.se2.bopit.domain;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameEngine {
    private CountDownTimer timer;
    private GameEngineListener listener;

    private int score = 0;
    private boolean isOverTime = false;
    private boolean miniGameLost = false;
    private Random rand;

    private ArrayList<Class<?>> miniGames;

    public GameEngine() {
        listener = null;

        //Add MiniGame Classes here like ColorButtonMinigame.class
        miniGames = new ArrayList<>(
                Arrays.asList(
                        ColorButtonMiniGame.class,
                        SimpleTextButtonMiniGame.class,
                        WeirdTextButtonMiniGame.class
                )
        );
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
        minigame.setGameListener(onGameResult);
    }

    private MiniGame getMiniGame(){
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
    private CountDownTimer startCountDown(long time){
        return new CountDownTimer(time, 5) {
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
     * Listener for Minigames, updates scores on event call and resets timer
     * Calls the onScoreUpdate, onGameEnd Listener to display the Score
     */
    private final GameListener onGameResult = new GameListener() {
        @Override
        public void onGameResult(boolean result) {
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

        }
    };

    /**
     * @param listener - Listener to add to the Engine
     * Adds a Listener to the engine
     */
    public void setGameEngineListener(GameEngineListener listener){
        if(listener != null)
            this.listener = listener;
    }

}
