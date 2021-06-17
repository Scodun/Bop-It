package com.se2.bopit.domain.engine;

import android.os.CountDownTimer;
import android.util.Log;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.MinigameAchievementCounters;
import com.se2.bopit.domain.interfaces.*;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.responsemodel.ResponseModel;
import com.se2.bopit.ui.DifficultyActivity;
import com.se2.bopit.ui.games.*;

/**
 * GameEngine Client used by UI on each device.
 * Depending on gameMode GameEngine connects to local or remote server.
 */
public class GameEngine {
    static final String TAG = GameEngine.class.getSimpleName();

    GameEngineListener listener;
    private GameEngineDataProvider dataProvider;
    private String userId;

    private static final String EASY = "easy";
    private static final String MEDIUM = "medium";

    private int score = 0;
    private boolean isOverTime = false;
    private boolean miniGameLost = false;
  
    boolean lifecycleCancel = false;
    CountDownTimer timer;
    private boolean isMyTurn;
    private long timeRemaining;
    private String currentUserId;

    MiniGamesProvider miniGamesProvider;
    PlatformFeaturesProvider platformFeaturesProvider;

    public GameEngine(
            MiniGamesProvider miniGamesProvider,
            PlatformFeaturesProvider platformFeaturesProvider,
            GameEngineListener listener,
            GameEngineDataProvider dataProvider) {
        this.miniGamesProvider = miniGamesProvider;
        this.platformFeaturesProvider = platformFeaturesProvider;
        this.listener = listener;
        this.setDataProvider(dataProvider);
        dataProvider.setGameEngineClient(this);
        this.setUserId(dataProvider.getUserId());
    }

    /**
     * Starts a new MiniGame
     * Calls getTimeForMiniGame to initialize the Time
     * This time will be used for the countdown.
     * <p>
     * Calls the MainActivity onGameStart Listener to display the Fragment
     * Sets the GameListener for the MiniGame
     */
    public void startNewGame() {
        getDataProvider().readyToStart(getUserId());
    }

    public void startNewGame(GameRoundModel round) {
        setMyTurn(getUserId().equals(round.getCurrentUserId()));
        setCurrentUserId(round.getCurrentUserId());

        MiniGame minigame = miniGamesProvider.createMiniGame(round);
        long time = minigame.getTime(DifficultyActivity.difficulty, score);

        timer = startCountDown(time);
        if (this.listener != null)
            listener.onGameStart(minigame, time);

        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);

        if (!isMyTurn())
            return;

        minigame.setGameListener(result -> {
            timer.cancel();
            if (listener == null)
                return;

            if (result && !isOverTime() && !isMiniGameLost()) {
                winMinigame();
            } else if (!lifecycleCancel) {
                setMiniGameLost(true);
                loseMinigame();
            }
        });
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
        getDataProvider().setClientCheated(getUserId());
    }

    public void resumeCountDown() {
        timer = startCountDown(timeRemaining);
    }

    public void onTick(long millisUntilFinished) {
        if (listener != null) {
            listener.onTimeTick(millisUntilFinished);
        }
        timeRemaining = millisUntilFinished;
    }

    public void onFinish() {
        if (isMyTurn() && listener != null) {
            setOverTime(true);
            Log.d(TAG, "timeout");
            loseMinigame();
        }
    }


    public void stopCurrentGame() {
        Log.d(TAG, "stopCurrentGame");
        if (!lifecycleCancel) {
            lifecycleCancel = true;
            if (timer != null)
                timer.cancel();
            getDataProvider().stopCurrentGame(getUserId());
            listener.onGameEnd(getScore());
        }
    }

    public void setCounter(MiniGame minigame) {
        if (minigame.getClass().equals(ImageButtonMinigame.class)) {
            MinigameAchievementCounters.setCounterImageButtonMinigame(MinigameAchievementCounters.getCounterImageButtonMinigame() + 1);
        } else if (minigame.getClass().equals(SimpleTextButtonMiniGame.class) ||
                minigame.getClass().equals(WeirdTextButtonMiniGame.class)) {
            MinigameAchievementCounters.setCounterTextBasedMinigame(MinigameAchievementCounters.getCounterTextBasedMinigame() + 1);
        } else if (minigame.getClass().equals(ShakePhoneMinigame.class)) {
            MinigameAchievementCounters.setCounterShakePhoneMinigame(MinigameAchievementCounters.getCounterShakePhoneMinigame() + 1);
        } else if (minigame.getClass().equals(PlacePhoneMiniGame.class)) {
            MinigameAchievementCounters.setCounterPlacePhoneMinigame(MinigameAchievementCounters.getCounterPlacePhoneMinigame() + 1);
        } else if (minigame.getClass().equals(CoverLightSensorMiniGame.class)) {
            MinigameAchievementCounters.setCounterCoverLightSensorMinigame(MinigameAchievementCounters.getCounterCoverLightSensorMinigame() + 1);
        } else if (minigame.getClass().equals(ColorButtonMiniGame.class)) {
            MinigameAchievementCounters.setCounterColorButtonMinigame(MinigameAchievementCounters.getCounterColorButtonMinigame() + 1);
        } else if (minigame.getClass().equals(SliderMinigame.class)) {
            MinigameAchievementCounters.setCounterSliderMinigame(MinigameAchievementCounters.getCounterSliderMinigame() + 1);
        } else if (minigame.getClass().equals(DrawingMinigame.class)) {
            MinigameAchievementCounters.setCounterDrawingMinigame(MinigameAchievementCounters.getCounterDrawingMinigame() + 1);
        } else if (minigame.getClass().equals(VolumeButtonMinigame.class)) {
            MinigameAchievementCounters.setCounterVolumeButtonMinigame(MinigameAchievementCounters.getCounterVolumeButtonMinigame() + 1);
        } else if (minigame.getClass().equals(RightButtonCombination.class)) {
            MinigameAchievementCounters.setCounterRightButtonsMinigame(MinigameAchievementCounters.getCounterRightButtonsMinigame() + 1);
        }
    }

    public void notifyGameResult(boolean result, ResponseModel responseModel, User user) {
        timer.cancel();
        if (getUserId().equals(user.getId()))
            listener.onLifeUpdate(user.getLives());
        if (!isMyTurn()) {
            listener.onScoreUpdate(getScore());
        }
        if (!lifecycleCancel)
            startNewGame();
    }


    public void reportCheat() {
        getDataProvider().detectCheating();
    }

    public void cheaterDetected(String userId) {
        if (this.getUserId().equals(userId)) {
            setOverTime(true);
            listener.onGameEnd(getScore());
            getDataProvider().sendGameResult(userId, false, null);
            Log.d(TAG, "cheater detected, game ending " + userId);
            getDataProvider().disconnect();
        }
    }

    public void loseMinigame() {
        getDataProvider().sendGameResult(getUserId(), false, null);
        setMiniGameLost(false);
        setOverTime(false);
    }

    public void winMinigame() {
        getDataProvider().sendGameResult(getUserId(), true, null);
        setScore(getScore() + 1);
        listener.onScoreUpdate(getScore());
    }

    public GameEngineDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(GameEngineDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isOverTime() {
        return isOverTime;
    }

    public void setOverTime(boolean overTime) {
        isOverTime = overTime;
    }

    public boolean isMiniGameLost() {
        return miniGameLost;
    }

    public void setMiniGameLost(boolean miniGameLost) {
        this.miniGameLost = miniGameLost;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
