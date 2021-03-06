package com.se2.bopit.domain;

import com.se2.bopit.domain.data.SinglePlayerGameEngineDataProvider;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.domain.mock.MiniGameMock;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class MinigameAchievementCountersTest {

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

    MiniGameMock gameMock;

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
        gameMock = new MiniGameMock();
        when(miniGamesProviderMock.createRandomMiniGame())
                .thenReturn(gameMock);
        SinglePlayerGameEngineDataProvider dataProvider = new SinglePlayerGameEngineDataProvider();
        gameEngine = new GameEngine(miniGamesProviderMock, platformProviderMock, listenerMock, dataProvider);
    }

    @After
    public void tearDown() throws Exception {
        reset(miniGamesProviderMock, listenerMock, platformProviderMock);
    }

    @Test
    public void getImageButtonMinigameCounter() {
        gameEngine.setCounter(imageButtonMinigame);
        assertEquals(1, MinigameAchievementCounters.getImageButtonMinigameCounter());
    }

    @Test
    public void getCounterColorButtonMinigame() {
        gameEngine.setCounter(colorButtonMiniGame);
        assertEquals(1, MinigameAchievementCounters.getCounterColorButtonMinigame());
    }

    @Test
    public void getCounterShakePhoneMinigame() {
        gameEngine.setCounter(shakePhoneMinigame);
        assertEquals(1, MinigameAchievementCounters.getCounterShakePhoneMinigame());
    }

    @Test
    public void getCounterTextBasedMinigame() {
        gameEngine.setCounter(simpleTextButtonMiniGame);
        gameEngine.setCounter(weirdTextButtonMiniGame);
        assertEquals(2, MinigameAchievementCounters.getCounterTextBasedMinigame());
    }

    @Test
    public void getCounterPlacePhoneMinigame() {
        gameEngine.setCounter(placePhoneMiniGame);
        assertEquals(1, MinigameAchievementCounters.getCounterPlacePhoneMinigame());
    }

    @Test
    public void getCounterRightButtonsMinigame() {
        gameEngine.setCounter(rightButtonCombination);
        assertEquals(1, MinigameAchievementCounters.getCounterRightButtonsMinigame());
    }

    @Test
    public void getCounterCoverLightSensorMinigame() {
        gameEngine.setCounter(coverLightSensorMiniGame);
        assertEquals(1, MinigameAchievementCounters.getCounterCoverLightSensorMinigame());
    }

    @Test
    public void getCounterSliderMinigame() {
        gameEngine.setCounter(sliderMinigame);
        assertEquals(1, MinigameAchievementCounters.getCounterSliderMinigame());
    }

    @Test
    public void getCounterVolumeButtonMinigame() {
        gameEngine.setCounter(volumeButtonMinigame);
        assertEquals(1, MinigameAchievementCounters.getCounterVolumeButtonMinigame());
    }

    @Test
    public void getCounterDrawingMinigame() {
        gameEngine.setCounter(drawingMinigame);
        assertEquals(1, MinigameAchievementCounters.getCounterDrawingMinigame());
    }

    @Test
    public void resetCounter() {
        gameEngine.setCounter(imageButtonMinigame);
        gameEngine.setCounter(colorButtonMiniGame);
        gameEngine.setCounter(sliderMinigame);
        gameEngine.setCounter(coverLightSensorMiniGame);

        MinigameAchievementCounters.resetCounter();
        assertEquals(0, MinigameAchievementCounters.getCounterColorButtonMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterCoverLightSensorMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterShakePhoneMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterSliderMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterDrawingMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterImageButtonMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterPlacePhoneMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterTextBasedMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterRightButtonsMinigame());
        assertEquals(0, MinigameAchievementCounters.getCounterVolumeButtonMinigame());
    }
}