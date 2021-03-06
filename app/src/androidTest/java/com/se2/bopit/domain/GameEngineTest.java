package com.se2.bopit.domain;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.se2.bopit.domain.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.concurrent.Callable;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GameEngineTest {

    private boolean isGameEnd = false;

    @Test
    public void startNewGame() {
        getInstrumentation().runOnMainSync(() -> {
            // simulate GameActivity
            MiniGamesRegistry miniGamesProvider = MiniGamesRegistry.getInstance();
            AndroidPlatformFeaturesProvider platformFeaturesProvider = new AndroidPlatformFeaturesProvider();
            SinglePlayerGameEngineDataProvider dataProvider = new SinglePlayerGameEngineDataProvider();

            GameEngine engine = new GameEngine(miniGamesProvider, platformFeaturesProvider, new GameEngineListener() {
                @Override
                public void onGameEnd(int score) {
                    assertEquals(0, score);
                }

                @Override
                public void onScoreUpdate(int score) {
                    fail();
                }

                @Override
                public void onLifeUpdate(int life) {
                    isGameEnd = true;
                }

                @Override
                public void onGameStart(MiniGame game, long time) {
                    assertNotNull(game);
                    assertTrue(time > 0);
                }

                @Override
                public void onTimeTick(long time) {
                    assertTrue(time > 0);
                }


            }, dataProvider);
            String singleUserId = "Player";
            engine.setUserId(singleUserId);
            new GameEngineServer(
                    miniGamesProvider,
                    platformFeaturesProvider,
                    Collections.singletonMap(singleUserId, new User(singleUserId, singleUserId)),
                    dataProvider);

            engine.startNewGame();
        });
        await().atMost(9, SECONDS).until(onGameEnd());
        assertTrue(isGameEnd);
    }

    private Callable<Boolean> onGameEnd() {
        return () -> isGameEnd;
    }
}