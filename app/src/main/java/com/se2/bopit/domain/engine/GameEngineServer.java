package com.se2.bopit.domain.engine;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.se2.bopit.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.ResponseModel;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GameEngine Server.
 *
 * This class is neither access UI nor is accessed by UI.
 * GameEngine communicates with Server via DataProvider
 */
public class GameEngineServer {
    static final String TAG = GameEngineServer.class.getSimpleName();

    final Map<String, User> users;

    final Set<String> usersReady = new HashSet<>();
    final Set<String> activePlayers = new HashSet<>();

    public GameEngineDataProvider dataProvider;

    int round = 1;
    GameRoundModel currentRound;
    GameModel<? extends ResponseModel> currentGame;

//    boolean isOverTime = false;
//    boolean miniGameLost = false;
//    boolean lifecycleCancel = false;
//    CountDownTimer timer;

    MiniGamesProvider miniGamesProvider;
    PlatformFeaturesProvider platformFeaturesProvider;

    Gson gson = new Gson();

    public GameEngineServer(MiniGamesProvider miniGamesProvider,
                            PlatformFeaturesProvider platformFeaturesProvider,
                            Map<String, User> users,
                            GameEngineDataProvider dataProvider) {
        this.miniGamesProvider = miniGamesProvider;
        this.platformFeaturesProvider = platformFeaturesProvider;
        this.users = new HashMap<>(users);
        activePlayers.addAll(users.keySet());
        this.dataProvider = dataProvider;
        dataProvider.setGameEngineServer(this);
        Log.d(TAG, "init");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void readyToStart(String userId) {
        Log.d(TAG, "readyToStart " + userId + " round #" + round);
        usersReady.add(userId);

        if(usersReady.size() == users.size()) {
            Log.d(TAG, "All players ready, starting round...");
            startNewGame();
        }
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startNewGame() {
        Log.d(TAG, "startNewGame round #" + round + "...");
//        GameRoundModel lastRound = currentRound;
        User nextPlayer = selectNextRoundUser();
        if(nextPlayer == null) {
            Log.d(TAG, "No active users left -> game over after " + round + " round");
            dataProvider.notifyGameOver();
            return;
        }
        currentRound = new GameRoundModel();
        currentRound.round = round++; // start with round 1
        currentRound.currentUserId = nextPlayer.getId();
        long time = (long) (Math.exp(-nextPlayer.getScore() * 0.08 + 7) + 2000);
        currentRound.time = time;
        MiniGame minigame = getMiniGame();
        currentRound.gameType = minigame.getClass().getSimpleName();
        currentGame = minigame.getModel();
        if(currentGame != null) {
            currentRound.modelType = currentGame.getClass().getSimpleName();
            currentRound.modelJson = gson.toJson(currentGame);
        }
        Log.d(TAG, "sending currentRound to data provider: " + currentRound);
        dataProvider.startNewGame(currentRound);
//        isOverTime = false;
//        miniGameLost = false;
        //timer = startCountDown(time);

//        minigame.setPlatformFeaturesProvider(platformFeaturesProvider);
//        minigame.setGameListener(result -> {
//            timer.cancel();
//            if (listener != null) {
//                if (result && !isOverTime && !miniGameLost) {
//                    score++;
//                    listener.onScoreUpdate(score);
//                    startNewGame();
//                } else if (!lifecycleCancel) {
//                    miniGameLost = true;
//                    listener.onGameEnd(score);
//                }
//            }
//
//        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    User selectNextRoundUser() {
        List<User> pool = users.values().stream()
                .filter(u -> activePlayers.contains(u.getId()))
                .collect(Collectors.toList());
        if(!pool.isEmpty()) {
            Collections.shuffle(pool);
            return pool.get(0);
        }
        return null;
    }

    private MiniGame getMiniGame() {
        return miniGamesProvider.createRandomMiniGame();
    }

//    /**
//     * @param time - countdown time in ms
//     *             Starts a new countdown
//     *             Calls the MainActivity onTimeTick, onFinish listener to display the time
//     */
//    private CountDownTimer startCountDown(long time) {
//        return platformFeaturesProvider.createCountDownTimer(
//                time, 5, this::onTick, this::onFinish)
//                .start();
//    }

//    public void onTick(long millisUntilFinished) {
//        // unused
//    }

//    public void onFinish() {
//        isOverTime = true;
////        if (listener != null)
////            listener.onGameEnd(score);
//        Log.d(TAG, "timeout");
//        dataProvider.notifyGameResult(false, null);
//    }


//    public void stopCurrentGame() {
//        if (!lifecycleCancel && !miniGameLost) {
//            lifecycleCancel = true;
////            timer.cancel();
//            miniGameLost = true;
//            //listener.onGameEnd(score);
//        }
//    }

    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        if(result) {
            Log.d(TAG, "User " + userId + " did the round #" + currentRound.round);
            users.get(userId)
                    .addScore();
        } else {
            Log.d(TAG, "User " + userId + " lost the round #" + currentRound.round);
            activePlayers.remove(userId);
        }
        dataProvider.notifyGameResult(result, responseModel);
    }

    public void stopCurrentGame(String userId) {
        Log.d(TAG, "Stop current game: " + userId);
        if(users.remove(userId) != null) {
            Log.d(TAG, "User " + userId + " left after round #" + currentRound.round);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public User[] getRoundResult() {
        return users.values()
                .stream()
                .sorted((u,v) -> Integer.compare(v.getScore(), u.getScore())) // sort by score
                .toArray(User[]::new);
    }

}
