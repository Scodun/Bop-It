package com.se2.bopit.domain;

import android.os.CountDownTimer;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.interfaces.*;
import com.se2.bopit.domain.mock.MiniGameMock;
import com.se2.bopit.ui.games.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.LongConsumer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Ignore("temporarily")
public class GameEngineUnitTest {
    ImageButtonMinigame imageButtonMinigame;
    ColorButtonMiniGame colorButtonMiniGame;
    ShakePhoneMinigame shakePhoneMinigame;
    SliderMinigame sliderMinigame;
    PlacePhoneMiniGame placePhoneMiniGame;
    CoverLightSensorMiniGame coverLightSensorMiniGame;
    VolumeButtonMinigame volumeButtonMinigame;
    SimpleTextButtonMiniGame simpleTextButtonMiniGame;
    WeirdTextButtonMiniGame weirdTextButtonMiniGame;
    RightButtonCombination rightButtonCombination;
    DrawingMinigame drawingMinigame;

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
    GameEngineDataProvider dataProviderMock;

    @Before
    public void setUp() throws Exception {
        imageButtonMinigame = new ImageButtonMinigame();
        colorButtonMiniGame = new ColorButtonMiniGame();
        shakePhoneMinigame = new ShakePhoneMinigame();
        sliderMinigame = new SliderMinigame();
        placePhoneMiniGame = new PlacePhoneMiniGame();
        coverLightSensorMiniGame = new CoverLightSensorMiniGame();
        volumeButtonMinigame = new VolumeButtonMinigame();
        simpleTextButtonMiniGame = new SimpleTextButtonMiniGame();
        weirdTextButtonMiniGame = new WeirdTextButtonMiniGame();
        rightButtonCombination = new RightButtonCombination();
        drawingMinigame = new DrawingMinigame();

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

        dataProviderMock = mock(GameEngineDataProvider.class);
        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, listenerMock,
                dataProviderMock);
    }

    @After
    public void tearDown() {
        reset(miniGamesProviderMock, listenerMock, platformProviderMock, timerMock);
    }

    private void mockGameActivity() {
        // mock GameActivity
        doAnswer(i -> {
            MiniGame game = i.getArgument(0);
            long time = i.getArgument(1);
            // simulate run game
            return null;
        }).when(listenerMock).onGameStart(any(), anyLong());
    }

    @Test
    public void startNewGame() {
        mockGameActivity();

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
        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, null, dataProviderMock);

        gameEngine.startNewGame();

        mockTimerOnTickHandler.accept(1000);

        gameMock.listener.onGameResult(false);

        mockTimerOnFinishHandler.run();
        //assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutGamesProvider() {
        gameEngine = new GameEngine(null, platformProviderMock, listenerMock, dataProviderMock);

        assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutPlatformProvider() {
        gameEngine = new GameEngine(miniGamesProviderMock, null, listenerMock, dataProviderMock);

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

    @Test
    public void testPauseCountDown() {
        mockGameActivity();

        gameEngine.startNewGame();
        gameEngine.pauseCountDown();
        assertFalse(timerRunning);
    }

    @Test
    public void testResumeCountDown() {
        mockGameActivity();

        gameEngine.startNewGame();
        gameEngine.resumeCountDown();
        assertTrue(timerRunning);
    }

}