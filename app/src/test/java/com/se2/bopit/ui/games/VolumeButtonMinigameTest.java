package com.se2.bopit.ui.games;

import androidx.fragment.app.Fragment;

import com.se2.bopit.domain.VolumeButton;
import com.se2.bopit.domain.VolumeButtonGameModel;
import com.se2.bopit.domain.interfaces.GameListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class VolumeButtonMinigameTest extends Fragment {

    VolumeButtonMinigame volumeButtonMinigame;
    VolumeButtonGameModel gameModel;

    @Before
    public void setUp() throws Exception {
        volumeButtonMinigame = new VolumeButtonMinigame();
        gameModel = VolumeButtonGameModel.createRandomModel();
    }

    @After
    public void tearDown() throws Exception {
        volumeButtonMinigame = null;
    }

    @Test
    public void setGameListener() {
        GameListener listener = r -> {
        };
        volumeButtonMinigame.setGameListener(listener);
        assertSame(listener, volumeButtonMinigame.gameModel.getGameListener());
    }

    @Test
    public void checkPressedKey() {
        volumeButtonMinigame.checkPressedKey();
        assertNull(volumeButtonMinigame.isCorrect);
    }

    @Test
    public void checkPressedKeyTrue() {
        GameListener listener = r -> {
        };
        volumeButtonMinigame.setGameListener(listener);
        volumeButtonMinigame.isCorrect = true;
        volumeButtonMinigame.checkPressedKey();
    }

    @Test
    public void getKeyEventIsDOWN() {
        volumeButtonMinigame.gameModel.correctResponse.volumeButton = VolumeButton.DOWN;
        assertEquals(25, volumeButtonMinigame.getKeyEvent());
    }

    @Test
    public void getKeyEventIsUP() {
        volumeButtonMinigame.gameModel.correctResponse.volumeButton = VolumeButton.UP;
        assertEquals(24, volumeButtonMinigame.getKeyEvent());
    }
}