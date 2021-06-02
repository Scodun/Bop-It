package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;
import com.se2.bopit.ui.games.DrawingMinigame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.PlacePhoneMiniGame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.ShakePhoneMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

public class GameEngine {
    GameEngineListener listener;


    int score = 0;
    boolean isOverTime = false;
    boolean miniGameLost = false;
    boolean lifecycleCancel = false;
    CountDownTimer timer;

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
     * Calls getTimeForMinigame to initialize the Time
     * This time will be used for the countdown.
     * <p>
     * Calls the MainActivity onGameStart Listener to display the Fragment
     * Sets the GameListener for the Minigame
     */
    public void startNewGame() {
        MiniGame minigame = getMiniGame();
        long time = getTimeForMinigame(minigame);
        timer = startCountDown(time);
        if (this.listener != null) {
            listener.onGameStart(minigame, time);
        }

        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);
        minigame.setGameListener(result -> {
            timer.cancel();
            if (listener != null) {
                if (result && !isOverTime && !miniGameLost) {
                    score++;
                    listener.onScoreUpdate(score);
                    startNewGame();
                } else if (!lifecycleCancel) {
                    miniGameLost = true;
                    listener.onGameEnd(score);
                }
            }

        });
    }

    /**
     * Sets the Time depending on the Minigame
     *
     * @param miniGame - the Minigame which Time should be set
     * @return - a specific Time to solve the Minigame
     */
    public long getTimeForMinigame(MiniGame miniGame){
        /*
         *Score 0   Time: 1692 ms
         *Score 1	Time: 1625 ms
         *Score 10	Time: 1192 ms
         *Score 20  Time: 944 ms
         *Score 30  Time: 821 ms
         *Score 50	Time: 729 ms
         *Score 100	Time: 700 ms
         */
        if(miniGame.getClass().equals(ImageButtonMinigame.class) ||
                miniGame.getClass().equals(SimpleTextButtonMiniGame.class) ||
                miniGame.getClass().equals(WeirdTextButtonMiniGame.class) ||
                miniGame.getClass().equals(ColorButtonMiniGame.class)){
            return (long) (Math.exp(-this.score*0.07+6.9)+700);
        }
        /*
         *Score 0   Time: 2596 ms
         *Score 1	Time: 2335 ms
         *Score 10	Time: 1925 ms
         *Score 20  Time: 1700 ms
         *Score 30  Time: 1594 ms
         *Score 50	Time: 1521 ms
         *Score 100	Time: 1500 ms
         */
        else if(miniGame.getClass().equals(CoverLightSensorMiniGame.class)){
            return (long) (Math.exp(-this.score*0.075+7)+1500);
        }
        /*
         *Score 0   Time: 4480 ms
         *Score 1	Time: 4197 ms
         *Score 10	Time: 2596 ms
         *Score 20  Time: 1903 ms
         *Score 30  Time: 1648 ms
         *Score 50	Time: 1520 ms
         *Score 100	Time: 1500 ms
         */
        else if(miniGame.getClass().equals(DrawingMinigame.class) ||
                miniGame.getClass().equals(PlacePhoneMiniGame.class)){
            return (long) (Math.exp(-this.score*0.1+8)+1500);
        }
        /*
         *Score 0   Time: 2608 ms
         *Score 1	Time: 1930 ms
         *Score 10	Time: 1401 ms
         *Score 20  Time: 1098 ms
         *Score 30  Time: 948 ms
         *Score 50	Time: 836 ms
         *Score 100	Time: 801 ms
         */
        else if(miniGame.getClass().equals(RightButtonCombination.class)){
            return (long) (Math.exp(-this.score*0.07+7.5)+800);
        }
        /*
         *Score 0   Time: 2998 ms
         *Score 1	Time: 2881 ms
         *Score 10	Time: 2096 ms
         *Score 20  Time: 1601 ms
         *Score 30  Time: 1330 ms
         *Score 50	Time: 1099 ms
         *Score 100	Time: 1004 ms
         */
        else if(miniGame.getClass().equals(ShakePhoneMinigame.class)){
            return (long) (Math.exp(-this.score*0.06+7.6)+1000);
        }
        else{
        /*
        *Score 1   Time: 3096 ms
        *Score 10  Time: 2500 ms
        *Score 20  Time: 2221 ms
        *Score 30  Time: 2100 ms
        *Score 50  Time: 2020 ms
        *Score 100 Time: 2000 ms
        */
            return (long) (Math.exp(-this.score * 0.08 + 7) + 2000);
        }
    }

    private MiniGame getMiniGame() {
        return miniGamesProvider.createRandomMiniGame();
    }

    /**
     * @param time - countdown time in ms
     *             Starts a new countdown
     *             Calls the MainActivity onTimeTick, onFinish listener to display the time
     */
    private CountDownTimer startCountDown(long time) {
        return platformFeaturesProvider.createCountDownTimer(
                time, 5, this::onTick, this::onFinish)
                .start();
    }

    public void onTick(long millisUntilFinished) {
        if (listener != null)
            listener.onTimeTick(millisUntilFinished);
    }

    public void onFinish() {
        isOverTime = true;
        if (listener != null)
            listener.onGameEnd(score);
    }


    public void stopCurrentGame() {
        if (!lifecycleCancel && !miniGameLost) {
            lifecycleCancel = true;
            timer.cancel();
            miniGameLost = true;
            listener.onGameEnd(score);
        }
    }

}
