package com.se2.bopit.domain;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
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

/**
 * GameEngine Client used by UI on each device.
 * Depending on gameMode GameEngine connects to local or remote server.
 */
public class GameEngine {
    static final String TAG = GameEngine.class.getSimpleName();

    GameEngineListener listener;
    public GameEngineDataProvider dataProvider;
    public String userId;


    int score = 0;
    boolean isOverTime = false;
    boolean miniGameLost = false;
    boolean lifecycleCancel = false;
    CountDownTimer timer;
    private long timeRemaining;
    public boolean isMyTurn;
    public String currentUserId;

    MiniGamesProvider miniGamesProvider;
    PlatformFeaturesProvider platformFeaturesProvider;

    public GameEngine(MiniGamesProvider miniGamesProvider,
                      PlatformFeaturesProvider platformFeaturesProvider,
                      GameEngineListener listener, GameEngineDataProvider dataProvider) {
        this.miniGamesProvider = miniGamesProvider;
        this.platformFeaturesProvider = platformFeaturesProvider;
        this.listener = listener;
        this.dataProvider = dataProvider;
        dataProvider.setGameEngineClient(this);
        this.userId = dataProvider.getUserId();
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
        dataProvider.readyToStart(userId);
    }

    public void startNewGame(GameRoundModel round) {
        isMyTurn = userId.equals(round.currentUserId);
        currentUserId = round.currentUserId;

        MiniGame minigame = miniGamesProvider.createMiniGame(round);
        long time = getTimeForMinigame(minigame);
        //TODO: add getTimeForMinigame to round: long time = round.time; //(long) (Math.exp(-this.score * 0.08 + 7) + 2000);

        timer = startCountDown(time);
        if (this.listener != null) {
            listener.onGameStart(minigame, time);
        }

        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);
        if (isMyTurn) {
            minigame.setGameListener(result -> {
                timer.cancel();
                if (listener != null) {
                    if (result && !isOverTime && !miniGameLost) {
                        dataProvider.sendGameResult(userId, true, null); // TODO!
                        score++;
                        listener.onScoreUpdate(score);
                        //startNewGame();
                    } else if (!lifecycleCancel) {
                        miniGameLost = true;
                        // TODO ?
                        dataProvider.sendGameResult(userId, false, null);
                        //listener.onGameEnd(score);
                    }
                }
            });
        }
    }
     /**
     * Sets the Time depending on the Minigame
     *
     * @param miniGame - the Minigame which Time should be set
     * @return - a specific Time to solve the Minigame
     */
    public long getTimeForMinigame(MiniGame miniGame){

        return (long) 9000;

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

    public void pauseCountDown() {
        timer.cancel();
        dataProvider.setClientCheated(userId);
    }

    public void resumeCountDown(){
        timer = startCountDown(timeRemaining);
    }

    public void onTick(long millisUntilFinished) {
        if (listener != null) {
            listener.onTimeTick(millisUntilFinished);
        }
        timeRemaining = millisUntilFinished;
    }

    public void onFinish() {
        if (isMyTurn && listener != null) {
            isOverTime = true;
            //listener.onGameEnd(score);
            Log.d(TAG, "timeout");
            dataProvider.sendGameResult(userId, false, null);
        }
    }


    public void stopCurrentGame() {
        Log.d(TAG, "stopCurrentGame");
        if (!lifecycleCancel) {
            lifecycleCancel = true;
            timer.cancel();
            dataProvider.stopCurrentGame(userId);
            listener.onGameEnd(score);
        }
    }

    public void notifyGameResult(boolean result, ResponseModel responseModel) {
        timer.cancel();
        if(!isMyTurn) {
            // TODO
            listener.onScoreUpdate(score);
        }
        if(!lifecycleCancel)
            startNewGame();
    }

    public void reportCheat(){
        dataProvider.detectCheating();
    }

    public void cheaterDetected(String userId){
        if(this.userId.equals(userId)) {
            isOverTime = true;
            listener.onGameEnd(score);
            dataProvider.sendGameResult(userId, false, null);
            dataProvider.disconnect();
        }
    }
}
