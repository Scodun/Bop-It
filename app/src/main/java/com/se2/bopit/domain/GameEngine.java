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
