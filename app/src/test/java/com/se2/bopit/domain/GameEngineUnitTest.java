package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.domain.mock.MiniGameMock;
import com.se2.bopit.ui.games.ColorButtonMiniGame;

import org.junit.Before;
import org.junit.Test;

import java.util.function.LongConsumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameEngineUnitTest {

    GameEngine gameEngine;
    MiniGamesProvider miniGamesProviderMock;
    GameEngineListener listenerMock;
    PlatformFeaturesProvider platformProviderMock;
    CountDownTimer timerMock;

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

        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, listenerMock);
    }

    @Test
    public void startNewGame() {
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

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < i + 1; j++ ) {
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
}