package com.se2.bopit.domain;

import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.mock.MiniGameMock;
import com.se2.bopit.domain.providers.MiniGamesProvider;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.ui.DifficultyActivity;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;
import com.se2.bopit.ui.games.DrawingMinigame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.PlacePhoneMiniGame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.ShakePhoneMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.SliderMinigame;
import com.se2.bopit.ui.games.VolumeButtonMinigame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import org.junit.After;
import org.junit.Before;
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

        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, listenerMock);
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
        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, null);

        gameEngine.startNewGame();

        mockTimerOnTickHandler.accept(1000);

        gameMock.listener.onGameResult(false);

        mockTimerOnFinishHandler.run();
        //assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutGamesProvider() {
        gameEngine = new GameEngine(null, platformProviderMock, listenerMock);

        assertThrows(NullPointerException.class, gameEngine::startNewGame);
    }

    @Test
    public void shouldFailWithoutPlatformProvider() {
        gameEngine = new GameEngine(miniGamesProviderMock, null, listenerMock);

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
    public void getTimeForMinigame(){
        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 6.9) + 1100), gameEngine.getTimeForMinigame(imageButtonMinigame));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 6.9) + 700), gameEngine.getTimeForMinigame(imageButtonMinigame));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 6.9) + 300), gameEngine.getTimeForMinigame(imageButtonMinigame));


        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.075 + 7) + 2000), gameEngine.getTimeForMinigame(coverLightSensorMiniGame));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.075 + 7) + 1500), gameEngine.getTimeForMinigame(coverLightSensorMiniGame));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.075 + 7) + 1000), gameEngine.getTimeForMinigame(coverLightSensorMiniGame));


        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.1 + 8) + 2000), gameEngine.getTimeForMinigame(drawingMinigame));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.1 + 8) + 1500), gameEngine.getTimeForMinigame(drawingMinigame));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.1 + 8) + 1000), gameEngine.getTimeForMinigame(drawingMinigame));


        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 7.5) + 1300), gameEngine.getTimeForMinigame(rightButtonCombination));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 7.5) + 800), gameEngine.getTimeForMinigame(rightButtonCombination));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.07 + 7.5) + 300), gameEngine.getTimeForMinigame(rightButtonCombination));


        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.06 + 7.6) + 1400), gameEngine.getTimeForMinigame(shakePhoneMinigame));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.06 + 7.6) + 1000), gameEngine.getTimeForMinigame(shakePhoneMinigame));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.06 + 7.6) + 600), gameEngine.getTimeForMinigame(shakePhoneMinigame));


        DifficultyActivity.setDifficulty("easy");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.08 + 7) + 3000), gameEngine.getTimeForMinigame(sliderMinigame));

        DifficultyActivity.setDifficulty("medium");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.08 + 7) + 2000), gameEngine.getTimeForMinigame(sliderMinigame));

        DifficultyActivity.setDifficulty("hard");
        assertEquals((long) (Math.exp(-gameEngine.score * 0.08 + 7) + 1000), gameEngine.getTimeForMinigame(sliderMinigame));
    }

}