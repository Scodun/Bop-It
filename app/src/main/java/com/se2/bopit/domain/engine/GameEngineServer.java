package com.se2.bopit.domain.engine;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.interfaces.GameEngineDataProvider;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.responsemodel.ResponseModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GameEngine Server.
 * <p>
 * This class is neither access UI nor is accessed by UI.
 * GameEngine communicates with Server via DataProvider
 */
public class GameEngineServer {
    static final String TAG = GameEngineServer.class.getSimpleName();


    final Map<String, User> users;

    final Set<String> usersReady = new HashSet<>();

    private GameEngineDataProvider dataProvider;

    int round = 1;
    GameRoundModel currentRound;
    GameModel<? extends ResponseModel> currentGame;

    boolean miniGameLost = false;
    boolean lifecycleCancel = false;

    User nextPlayer;
    User lastPlayer;

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
        this.setDataProvider(dataProvider);
        dataProvider.setGameEngineServer(this);
        Log.d(TAG, "init");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void readyToStart(String userId) {
        Log.d(TAG, "readyToStart " + userId + " round #" + round);
        usersReady.add(userId);

        if (usersReady.size() == users.size()) {
            Log.d(TAG, "All players ready, starting round...");
            startNewGame();
        }
    }


    /**
     * Starts a new Minigame
     * <p>
     * Calls the MainActivity onGameStart Listener to display the Fragment
     * Sets the GameListener for the Minigame
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startNewGame() {
        Log.d(TAG, "startNewGame round #" + round + "...");

        nextPlayer = selectNextRoundUser();

        if (nextPlayer == null) {
            Log.d(TAG, "No active users left -> game over after " + round + " round");
            getDataProvider().notifyGameOver();
            return;
        }

        currentRound = new GameRoundModel();
        currentRound.setRound(round++); // start with round 1
        //for cheatfunction
        nextPlayer.setCheated(false);
        currentRound.setCurrentUserId(nextPlayer.getId());

        long time = (long) (Math.exp(-nextPlayer.getScore() * 0.08 + 7) + 2000);
        currentRound.setTime(time);

        MiniGame minigame = miniGamesProvider.createRandomMiniGame();
        currentRound.setGameType(minigame.getClass().getSimpleName());
        currentGame = minigame.getModel();

        if (currentGame != null) {
            currentRound.setModelType(currentGame.getClass().getSimpleName());
            currentRound.setModelJson(gson.toJson(currentGame));
        }

        //for cheat function
        lastPlayer = nextPlayer;

        Log.d(TAG, "sending currentRound to data provider: " + currentRound);
        getDataProvider().startNewGame(currentRound);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    User selectNextRoundUser() {
        List<User> pool = users.values().stream()
                .filter(u -> u.getLives() > 0)
                .collect(Collectors.toList());

        usersReady.clear();


        if (pool.isEmpty())
            return null;

        int chanceToRepeat = 5;
        if (currentRound != null && new Random().nextInt(100) < chanceToRepeat && users.get(currentRound.getCurrentUserId()).getLives() > 0)
            return users.get(currentRound.getCurrentUserId());

        Collections.shuffle(pool);
        return pool.get(0);
    }

    public void stopCurrentGame() {
        if (!lifecycleCancel && !miniGameLost) {
            lifecycleCancel = true;
            miniGameLost = true;
        }
    }

    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        User user = users.get(userId);
        if (user != null) {
            if (result) {
                Log.d(TAG, userId + " won the round #" + (currentRound != null ? currentRound.getRound() : "null"));
                user.incrementScore();
            } else {
                Log.d(TAG, userId + " lost the round #" + (currentRound != null ? currentRound.getRound() : "null"));
                user.loseLife();
            }
            getDataProvider().notifyGameResult(result, responseModel, user);
        }
    }

    public void stopCurrentGame(String userId) {
        Log.d(TAG, "Stop current game: " + userId);
        if (users.remove(userId) != null && currentRound != null)
            Log.d(TAG,  userId + " left after round #" + currentRound.getRound());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public User[] getRoundResult() {
        return users.values()
                .stream()
                .sorted((u, v) -> Integer.compare(v.getScore(), u.getScore())) // sort by score
                .toArray(User[]::new);
    }

    public void setClientCheated(String userId) {
        if (userId.equals(currentRound.getCurrentUserId())) {
            Log.d("CHEATER", "CHEATER");
            nextPlayer.setCheated(true);
        }
    }

    public void detectCheating(String reporterUserId) {
        if (nextPlayer.hasCheated()) {
            nextPlayer.loseAllLives();
            users.remove(nextPlayer.getId());
            usersReady.remove(nextPlayer.getId());
            getDataProvider().cheaterDetected(nextPlayer.getId());
        } else {
            User reporter = users.get(reporterUserId);
            reporter.loseLife();
            if (reporter.getLives() == 0) {
                usersReady.remove(reporterUserId);
                users.remove(reporterUserId);
            }
        }

        if (users.size() <= 1)
            getDataProvider().notifyGameOver();

    }

    public GameEngineDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(GameEngineDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
