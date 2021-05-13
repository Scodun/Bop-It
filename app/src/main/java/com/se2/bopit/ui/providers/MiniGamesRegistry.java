package com.se2.bopit.ui.providers;

import android.util.Log;

import com.se2.bopit.domain.GameRuleItemModel;
import com.se2.bopit.domain.GameRules;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import java.util.List;
import java.util.Random;

public class MiniGamesRegistry implements MiniGamesProvider {
    static final String TAG = MiniGamesRegistry.class.getSimpleName();

    static final Random RND = new Random();

    static Class<?>[] GAME_TYPES = {
            ColorButtonMiniGame.class,
            SimpleTextButtonMiniGame.class,
            WeirdTextButtonMiniGame.class,
            ImageButtonMinigame.class,
            RightButtonCombination.class
    };

    static MiniGamesRegistry instance;

    public final GameRules gameRules;

    protected MiniGamesRegistry(GameRules gameRules) {
        this.gameRules = gameRules;
        Log.d(TAG, "MiniGamesRegistry created");
    }

    public static MiniGamesRegistry getInstance() {
        if(instance == null) {
            instance = new MiniGamesRegistry(
                    new GameRules(GAME_TYPES));
        }
        return instance;
    }

    GameRuleItemModel lastGameType;

    @Override
    public MiniGame createRandomMiniGame() {
        List<GameRuleItemModel> items = gameRules.getEnabledItems();
        int itemsSize = items.size();
        boolean avoidRepeating = gameRules.avoidRepeatingGameTypes && itemsSize > 2;

        if(itemsSize == 0) {
            // ignore invalid settings
            Log.w(TAG, "No games enabled! => ignoring preferences.");
            items = gameRules.getItems();
        }

        GameRuleItemModel item;
        do {
            item = items.get(RND.nextInt(itemsSize));
        } while (avoidRepeating && item == lastGameType);

        lastGameType = item;
        return createMiniGame(item);
    }

    MiniGame createMiniGame(GameRuleItemModel model) {
        try {
            return (MiniGame) model.type
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error creating minigame from model: " + model, e);
            throw new RuntimeException(e);
        }
    }
}
