package com.se2.bopit.domain;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

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
    public boolean isMyTurn;

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
     * Initialises the Time e^(-score*0.08+7)+2000
     * This time will be used for the countdown:
     * Game 1   Time: 3096 ms
     * Game 10  Time: 2500 ms
     * Game 20  Time: 2221 ms
     * Game 30  Time: 2100 ms
     * Game 50  Time: 2020 ms
     * Game 100 Time: 2000 ms
     * <p>
     * Calls the MainActivity onGameStart Listener to display the Fragment
     * Sets the GameListener for the Minigame
     */
    public void startNewGame() {
        dataProvider.readyToStart(userId);
    }

    public void startNewGame(GameRoundModel round) {
        isMyTurn = userId.equals(round.currentUserId);

        MiniGame minigame = miniGamesProvider.createMiniGame(round);
        long time = round.time; //(long) (Math.exp(-this.score * 0.08 + 7) + 2000);
        timer = startCountDown(time);
        if (this.listener != null) {
            listener.onGameStart(minigame, time);
        }

        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);
        if(isMyTurn) {
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
        } else {
            minigame.setGameListener(result -> {
                Log.d(TAG, "not your turn");
            });
        }
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
        if (listener != null) {
            listener.onTimeTick(millisUntilFinished);
        }
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
}
