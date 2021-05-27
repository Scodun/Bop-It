package com.se2.bopit.domain;

import android.os.CountDownTimer;

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
    boolean isMyTurn;

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
            minigame.setGameListener(r -> {
                timer.cancel();
                if (listener != null) {
                    int roundScore = dataProvider.sendGameResult(userId, r, null); // TODO!
                    boolean result = roundScore != 0;
                    if (result && !isOverTime && !miniGameLost) {
                        score+=roundScore;
                        listener.onScoreUpdate(score);
                        startNewGame();
                    } else if (!lifecycleCancel) {
                        miniGameLost = true;
                        // TODO ?
                        listener.onGameEnd(score);
                    }
                }

            });
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
        if(isMyTurn) {
            isOverTime = true;
        }
        if (listener != null)
            listener.onGameEnd(score);
    }


    public void stopCurrentGame() {
        if (!lifecycleCancel && !miniGameLost) {
            dataProvider.stopCurrentGame(userId);
            lifecycleCancel = true;
            timer.cancel();
            miniGameLost = true;
            listener.onGameEnd(score);
        }
    }

}
