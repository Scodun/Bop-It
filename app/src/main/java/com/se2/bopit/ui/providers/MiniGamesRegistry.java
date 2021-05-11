package com.se2.bopit.ui.providers;

import android.util.Log;

import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.ShakePhoneMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import java.util.Random;

public class MiniGamesRegistry implements MiniGamesProvider {
    static final String TAG = MiniGamesRegistry.class.getSimpleName();

    static final Random RND = new Random();

    static Class<?>[] GAME_TYPES = {
            ColorButtonMiniGame.class,
            SimpleTextButtonMiniGame.class,
            WeirdTextButtonMiniGame.class,
            ImageButtonMinigame.class,
            RightButtonCombination.class,
            ShakePhoneMinigame.class
    };

    @Override
    public MiniGame createRandomMiniGame() {
        try {
            return (MiniGame) GAME_TYPES[RND.nextInt(GAME_TYPES.length)]
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error creating minigame.", e);
        }

        // fallback
        return new ColorButtonMiniGame();
    }
}
