package com.se2.bopit.domain;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;


public class GameEngineTest {

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