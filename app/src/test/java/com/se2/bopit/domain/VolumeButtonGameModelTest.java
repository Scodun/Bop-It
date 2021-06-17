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
    VolumeButtonModel wrongCheck;
    VolumeButtonModel rightCheck;

    List<VolumeButtonModel> volumeButtonModels;
    List<VolumeButtonModel> volumeButtonModelsCheck;

    @Before
    public void setUp() throws Exception {
        volumeButtonModels = new ArrayList<>();
        volumeButtonModelsCheck = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
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

        assertEquals(challengeCheck, gameModel.challenge);
    }

    @Test
    public void shuffleResponses() {
        gameModel = VolumeButtonGameModel.createRandomModel();
        volumeButtonModels = gameModel.responses;
        right = gameModel.correctResponse;
        volumeButtonModels.remove(right);

        do {
            gameModelCheck = VolumeButtonGameModel.createRandomModel();
            volumeButtonModelsCheck = gameModelCheck.responses;
            rightCheck = gameModelCheck.correctResponse;
            volumeButtonModelsCheck.remove(rightCheck);
        } while (right.label.equals(rightCheck.label));

        assertNotEquals(volumeButtonModels, volumeButtonModelsCheck);
    }
}