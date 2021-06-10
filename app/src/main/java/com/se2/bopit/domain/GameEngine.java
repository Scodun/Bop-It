package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
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

public class GameEngine {
    GameEngineListener listener;

    private static final String EASY = "easy";
    private static final String MEDIUM = "medium";

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
                    setCounter(minigame);
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
            IMAGEBUTTONMINIGAME
            SIMPLETEXTBUTTONMINIGAME
            WEIRDBUTTONMINIGAME
            COLORBUTTONMINIGAME
             */
            if (miniGame.getClass().equals(ImageButtonMinigame.class) ||
                    miniGame.getClass().equals(SimpleTextButtonMiniGame.class) ||
                    miniGame.getClass().equals(WeirdTextButtonMiniGame.class) ||
                    miniGame.getClass().equals(ColorButtonMiniGame.class)) {
                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.07 + 6.9) + 1600);
                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.07 + 6.9) + 1200);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.07 + 6.9) + 800);
                }
            }
            /*
            COVERLIGHTSENSORMINIGAME
             */
            else if (miniGame.getClass().equals(CoverLightSensorMiniGame.class)) {
                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.075 + 7) + 2000);
                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.075 + 7) + 1500);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.075 + 7) + 1000);
                }
            }
            /*
            DRAWINGMINIGAME
            PLACEPHONEMINIGAME
             */
            else if (miniGame.getClass().equals(DrawingMinigame.class) ||
                    miniGame.getClass().equals(PlacePhoneMiniGame.class)) {

                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.1 + 8) + 2000);
                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.1 + 8) + 1500);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.1 + 8) + 1000);
                }
            }
            /*
            RIGHTBUTTONCOMBINATION
             */
            else if (miniGame.getClass().equals(RightButtonCombination.class)) {
                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.07 + 7.5) + 1800);

                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.07 + 7.5) + 1300);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.07 + 7.5) + 800);
                }
            }
            /*
            SHAKEPHONEMINIGAME
             */
            else if (miniGame.getClass().equals(ShakePhoneMinigame.class)) {
                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.06 + 7.6) + 1400);
                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.06 + 7.6) + 1000);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.06 + 7.6) + 600);
                }
            /*
            DEFAULT
            */
            } else {
                if(DifficultyActivity.difficulty.equals(EASY)) {
                    return (long) (Math.exp(-this.score * 0.08 + 7) + 3000);
                }
                else if(DifficultyActivity.difficulty.equals(MEDIUM)) {
                    return (long) (Math.exp(-this.score * 0.08 + 7) + 2000);
                }
                else{
                    return (long) (Math.exp(-this.score * 0.08 + 7) + 1000);
                }
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
    public void setCounter(MiniGame minigame){
        if(minigame.getClass().equals(ImageButtonMinigame.class)){
            MinigameAchievementCounters.counterImageButtonMinigame++;
        }
        else if(minigame.getClass().equals(SimpleTextButtonMiniGame.class)||
                minigame.getClass().equals(WeirdTextButtonMiniGame.class)){
            MinigameAchievementCounters.counterTextBasedMinigame++;
        }
        else if(minigame.getClass().equals(ShakePhoneMinigame.class)){
            MinigameAchievementCounters.counterShakePhoneMinigame++;
        }
        else if(minigame.getClass().equals(PlacePhoneMiniGame.class)){
            MinigameAchievementCounters.counterPlacePhoneMinigame++;
        }
        else if(minigame.getClass().equals(CoverLightSensorMiniGame.class)){
            MinigameAchievementCounters.counterCoverLightSensorMinigame++;
        }
        else if(minigame.getClass().equals(ColorButtonMiniGame.class)){
            MinigameAchievementCounters.counterColorButtonMinigame++;
        }
        else if(minigame.getClass().equals(SliderMinigame.class)){
            MinigameAchievementCounters.counterSliderMinigame++;
        }
        else if(minigame.getClass().equals(DrawingMinigame.class)){
            MinigameAchievementCounters.counterDrawingMinigame++;
        }
        else if(minigame.getClass().equals(VolumeButtonMinigame.class)){
            MinigameAchievementCounters.counterVolumeButtonMinigame++;
        }
        else if(minigame.getClass().equals(RightButtonCombination.class)){
            MinigameAchievementCounters.counterRightButtonsMinigame++;
        }
    }
}
