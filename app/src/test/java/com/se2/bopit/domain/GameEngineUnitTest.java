package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.mock.MiniGameMock;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.LongConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore("temporarily")
public class GameEngineUnitTest {

    GameEngine gameEngine;
    MiniGamesProvider miniGamesProviderMock;
    GameEngineListener listenerMock;
    PlatformFeaturesProvider platformProviderMock;
    CountDownTimer timerMock;

    MiniGameMock gameMock;

    LongConsumer mockTimerOnTickHandler;
    Runnable mockTimerOnFinishHandler;

    boolean timerRunning;
    long timerStartedAt;
    long timerStoppedAt;

    @Before
    public void setUp() throws Exception {
        miniGamesProviderMock = mock(MiniGamesProvider.class);
        listenerMock = mock(GameEngineListener.class);
        platformProviderMock = mock(PlatformFeaturesProvider.class);
        timerMock = mock(CountDownTimer.class);
        when(timerMock.start()).then(i -> {
            timerRunning = true;
            timerStartedAt = System.currentTimeMillis();
            return timerMock;
        });
        doAnswer(i -> {
            timerRunning = false;
            timerStoppedAt = System.currentTimeMillis();
            return null;
        }).when(timerMock).cancel();
        when(platformProviderMock.createCountDownTimer(anyLong(), anyLong(), any(), any()))
                .then(i -> {
                    mockTimerOnTickHandler = i.getArgument(2);
                    mockTimerOnFinishHandler = i.getArgument(3);
                    return timerMock;
                });

        gameMock = new MiniGameMock();
        when(miniGamesProviderMock.createRandomMiniGame())
                .thenReturn(gameMock);

        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, listenerMock, dataProvider);
    }

    @After
    public void tearDown() {
        reset(miniGamesProviderMock, listenerMock, platformProviderMock, timerMock);
    }

    @Test
    public void startNewGame() {

        // mock GameActivity
        doAnswer(i -> {
            MiniGame game = i.getArgument(0);
            long time = i.getArgument(1);
            // simulate run game
            return null;
        }).when(listenerMock).onGameStart(any(), anyLong());

        // check initial state
        assertEquals(0, gameEngine.score);
        assertFalse(gameEngine.isOverTime);
        assertFalse(gameEngine.miniGameLost);
        assertFalse(timerRunning);

        gameEngine.startNewGame();

        assertTrue(timerRunning);

        verify(platformProviderMock).createCountDownTimer(anyLong(), anyLong(), any(), any());
        verify(timerMock).start();
        //verifyNoMoreInteractions(timerMock);
        assertNotNull(mockTimerOnTickHandler);
        assertNotNull(mockTimerOnFinishHandler);
        assertNotNull(gameMock.listener);
        assertEquals(0, gameEngine.score);
        assertFalse(gameEngine.isOverTime);
        assertFalse(gameEngine.miniGameLost);


        // user gives 10 correct answers

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i + 1; j++) {
                mockTimerOnTickHandler.accept(System.currentTimeMillis() + j * 100);
            }

            //reset(timerMock);

            gameMock.listener.onGameResult(true);

            assertEquals(i + 1, gameEngine.score);
            verify(timerMock, times(i + 1)).cancel();
            verify(timerMock, times(i + 2)).start();
            //verifyNoMoreInteractions(timerMock);
            assertFalse(gameEngine.isOverTime);
        }

        // user fails to answer in time
        //reset(timerMock);
        mockTimerOnFinishHandler.run();
        verify(timerMock, times(10)).cancel();
        assertTrue(gameEngine.isOverTime);
        assertEquals(10, gameEngine.score);
    }

    // TODO this test checks status quo. Reconsider if game engine without listener should work at all
    @Test
    public void startNewGameWithoutListener() {
        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, null, dataProvider);

        gameEngine.startNewGame();

        mockTimerOnTickHandler.accept(1000);

        gameMock.listener.onGameResult(false);

        mockTimerOnFinishHandler.run();
        //assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutGamesProvider() {
        gameEngine = new GameEngine(null, platformProviderMock, listenerMock, dataProvider);

        assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutPlatformProvider() {
        gameEngine = new GameEngine(miniGamesProviderMock, null, listenerMock, dataProvider);

        assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void newGameEndsWithWrongAnswer() {
        MiniGameMock gameMock = new MiniGameMock();
        when(miniGamesProviderMock.createRandomMiniGame())
                .thenReturn(gameMock);

        // mock GameActivity
        doAnswer(i -> {
            MiniGame game = i.getArgument(0);
            long time = i.getArgument(1);
            // simulate run game
            return null;
        }).when(listenerMock).onGameStart(any(), anyLong());

        // check initial state
        assertEquals(0, gameEngine.score);
        assertFalse(gameEngine.isOverTime);
        assertFalse(gameEngine.miniGameLost);
        assertFalse(timerRunning);

        gameEngine.startNewGame();

        assertTrue(timerRunning);

        gameMock.listener.onGameResult(false);

        assertFalse(gameEngine.isOverTime);
        assertTrue(gameEngine.miniGameLost);
        assertEquals(0, gameEngine.score);
    }

}