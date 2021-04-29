package com.se2.bopit.domain;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GameEngineTest  {

    @Test
    public void startNewGame() {
        getInstrumentation().runOnMainSync(() -> {
            GameEngine engine = new GameEngine();
            engine.setGameEngineListener(gameEngineListener);
            engine.startNewGame();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private final GameEngineListener gameEngineListener = new GameEngineListener() {
        @Override
        public void onGameEnd(int score) {
            assertEquals(score,0);
        }

        @Override
        public void onScoreUpdate(int score) {
            fail();
        }

        @Override
        public void onGameStart(MiniGame game, long time) {
            assertNotNull(game);
            assertTrue(time>0);
        }

        @Override
        public void onTimeTick(long time) {
            assertTrue(time>0);
        }
    };
}