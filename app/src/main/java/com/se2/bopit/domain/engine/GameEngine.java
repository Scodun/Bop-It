package com.se2.bopit.domain.engine;

import android.os.CountDownTimer;
import android.util.Log;

import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.MinigameAchievementCounters;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.responsemodel.ResponseModel;
import com.se2.bopit.ui.DifficultyActivity;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;
import com.se2.bopit.ui.games.DrawingMinigame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.PlacePhoneMiniGame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.ShakePhoneMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.SliderMinigame;
import com.se2.bopit.ui.games.VolumeButtonMinigame;
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

    private static final String EASY = "easy";
    private static final String MEDIUM = "medium";

    public int score = 0;
    public boolean isOverTime = false;
    public boolean miniGameLost = false;
    boolean lifecycleCancel = false;
    CountDownTimer timer;
    public boolean isMyTurn;
    private long timeRemaining;
    public String currentUserId;

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
        if (this.listener != null)
            listener.onGameStart(minigame, time);

        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);

        if (!isMyTurn)
            return;

        minigame.setGameListener(result -> {
            timer.cancel();
            if (listener == null)
                return;

            if (result && !isOverTime && !miniGameLost) {
                winMinigame();
            } else if (!lifecycleCancel) {
                miniGameLost = true;
                // TODO ?
                loseMinigame();
            }
        });
    }

    /**
     * Sets the Time depending on the Minigame
     *
     * @param miniGame - the Minigame which Time should be set
     * @return - a specific Time to solve the Minigame
     */
    public long getTimeForMinigame(MiniGame miniGame) {
            /*
            IMAGEBUTTONMINIGAME
            SIMPLETEXTBUTTONMINIGAME
            WEIRDBUTTONMINIGAME
            COLORBUTTONMINIGAME
             */
        if (miniGame.getClass().equals(ImageButtonMinigame.class) ||
                miniGame.getClass().equals(SimpleTextButtonMiniGame.class) ||
                miniGame.getClass().equals(WeirdTextButtonMiniGame.class) ||
                miniGame.getClass().equals(ColorButtonMiniGame.class)) {
            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.07 + 6.9) + 1600);
            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.07 + 6.9) + 1200);
            } else {
                return (long) (Math.exp(-this.score * 0.07 + 6.9) + 800);
            }
        }
            /*
            COVERLIGHTSENSORMINIGAME
             */
        else if (miniGame.getClass().equals(CoverLightSensorMiniGame.class)) {
            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.075 + 7) + 2000);
            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.075 + 7) + 1500);
            } else {
                return (long) (Math.exp(-this.score * 0.075 + 7) + 1000);
            }
        }
            /*
            DRAWINGMINIGAME
            PLACEPHONEMINIGAME
             */
        else if (miniGame.getClass().equals(DrawingMinigame.class) ||
                miniGame.getClass().equals(PlacePhoneMiniGame.class)) {

            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.1 + 8) + 2000);
            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.1 + 8) + 1500);
            } else {
                return (long) (Math.exp(-this.score * 0.1 + 8) + 1000);
            }
        }
            /*
            RIGHTBUTTONCOMBINATION
             */
        else if (miniGame.getClass().equals(RightButtonCombination.class)) {
            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.07 + 7.5) + 1800);

            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.07 + 7.5) + 1300);
            } else {
                return (long) (Math.exp(-this.score * 0.07 + 7.5) + 800);
            }
        }
            /*
            SHAKEPHONEMINIGAME
             */
        else if (miniGame.getClass().equals(ShakePhoneMinigame.class)) {
            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.06 + 7.6) + 1400);
            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.06 + 7.6) + 1000);
            } else {
                return (long) (Math.exp(-this.score * 0.06 + 7.6) + 600);
            }
            /*
            DEFAULT
            */
        } else {
            if (DifficultyActivity.difficulty.equals(EASY)) {
                return (long) (Math.exp(-this.score * 0.08 + 7) + 3000);
            } else if (DifficultyActivity.difficulty.equals(MEDIUM)) {
                return (long) (Math.exp(-this.score * 0.08 + 7) + 2000);
            } else {
                return (long) (Math.exp(-this.score * 0.08 + 7) + 1000);
            }
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

    public void pauseCountDown() {
        timer.cancel();
        dataProvider.setClientCheated(userId);
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
        if (isMyTurn && listener != null) {
            isOverTime = true;
            //listener.onGameEnd(score);
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
            dataProvider.stopCurrentGame(userId);
            listener.onGameEnd(score);
        }
    }

    public void setCounter(MiniGame minigame) {
        if (minigame.getClass().equals(ImageButtonMinigame.class)) {
            MinigameAchievementCounters.counterImageButtonMinigame++;
        } else if (minigame.getClass().equals(SimpleTextButtonMiniGame.class) ||
                minigame.getClass().equals(WeirdTextButtonMiniGame.class)) {
            MinigameAchievementCounters.counterTextBasedMinigame++;
        } else if (minigame.getClass().equals(ShakePhoneMinigame.class)) {
            MinigameAchievementCounters.counterShakePhoneMinigame++;
        } else if (minigame.getClass().equals(PlacePhoneMiniGame.class)) {
            MinigameAchievementCounters.counterPlacePhoneMinigame++;
        } else if (minigame.getClass().equals(CoverLightSensorMiniGame.class)) {
            MinigameAchievementCounters.counterCoverLightSensorMinigame++;
        } else if (minigame.getClass().equals(ColorButtonMiniGame.class)) {
            MinigameAchievementCounters.counterColorButtonMinigame++;
        } else if (minigame.getClass().equals(SliderMinigame.class)) {
            MinigameAchievementCounters.counterSliderMinigame++;
        } else if (minigame.getClass().equals(DrawingMinigame.class)) {
            MinigameAchievementCounters.counterDrawingMinigame++;
        } else if (minigame.getClass().equals(VolumeButtonMinigame.class)) {
            MinigameAchievementCounters.counterVolumeButtonMinigame++;
        } else if (minigame.getClass().equals(RightButtonCombination.class)) {
            MinigameAchievementCounters.counterRightButtonsMinigame++;
        }
    }

    public void notifyGameResult(boolean result, ResponseModel responseModel, User user) {
        timer.cancel();
        if (userId.equals(user.getId()))
            listener.onLifeUpdate(user.getLives());
        if (!isMyTurn) {
            // TODO
            listener.onScoreUpdate(score);
        }
        if (!lifecycleCancel)
            startNewGame();
    }


    public void reportCheat() {
        dataProvider.detectCheating();
    }

    public void cheaterDetected(String userId) {
        if (this.userId.equals(userId)) {
            isOverTime = true;
            listener.onGameEnd(score);
            dataProvider.sendGameResult(userId, false, null);
            Log.d(TAG, "cheater detected, game ending " + userId);
            dataProvider.disconnect();
        }
    }

    public void loseMinigame() {
        dataProvider.sendGameResult(userId, false, null);
        miniGameLost = false;
        isOverTime = false;
    }

    public void winMinigame() {
        dataProvider.sendGameResult(userId, true, null);
        score++;
        listener.onScoreUpdate(score);
    }
}
