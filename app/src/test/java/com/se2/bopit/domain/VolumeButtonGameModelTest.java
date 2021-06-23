package com.se2.bopit.domain;

import com.se2.bopit.domain.gamemodel.VolumeButtonGameModel;
import com.se2.bopit.domain.responsemodel.VolumeButtonModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class VolumeButtonGameModelTest {

    VolumeButtonGameModel gameModel;
    VolumeButtonGameModel gameModelCheck;

    VolumeButtonModel wrong;
    VolumeButtonModel right;
    VolumeButtonModel rightCheck;

    List<VolumeButtonModel> volumeButtonModels;
    List<VolumeButtonModel> volumeButtonModelsCheck;

    @Before
    public void setUp() {
        volumeButtonModels = new ArrayList<>();
        volumeButtonModelsCheck = new ArrayList<>();
    }

    @After
    public void tearDown() {
        volumeButtonModels = null;
    }

    @Test
    public void createRandomModelNotNullCheck() {
        gameModel = VolumeButtonGameModel.createRandomModel();

        assertNotNull(gameModel);
    }

    @Test
    public void rightVolumeButtonIsUP() {
        wrong = new VolumeButtonModel(VolumeButton.DOWN);
        right = new VolumeButtonModel(VolumeButton.UP);

        gameModel = VolumeButtonGameModel.createRandomModel();
        volumeButtonModels.add(wrong);
        volumeButtonModels.add(right);

        String challengeCheck = gameModel.getChallenge();

        assertEquals(challengeCheck, gameModel.getChallenge());
    }

    @Test
    public void shuffleResponses() {
        gameModel = VolumeButtonGameModel.createRandomModel();
        volumeButtonModels = gameModel.getResponses();
        right = gameModel.getCorrectResponse();
        volumeButtonModels.remove(right);

        do {
            gameModelCheck = VolumeButtonGameModel.createRandomModel();
            volumeButtonModelsCheck = gameModelCheck.getResponses();
            rightCheck = gameModelCheck.getCorrectResponse();
            volumeButtonModelsCheck.remove(rightCheck);
        } while (right.getLabel().equals(rightCheck.getLabel()));

        assertNotEquals(volumeButtonModels, volumeButtonModelsCheck);
    }
}