package com.se2.bopit.domain;

import com.se2.bopit.domain.gamemodel.RightButtonCombinationModel;
import com.se2.bopit.domain.responsemodel.RightButtonModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RightButtonCombinationModelTest {

    RightButtonCombinationModel rightButtonCombinationModel;

    RightButtonModel rightButton;
    RightButtonModel secondRightButton;

    RightButtonModel wrongButton;
    RightButtonModel secondWrongButton;

    List<RightButtonModel> wrongAnswers;

    @Before
    public void setUp() throws Exception {
        wrongAnswers = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        wrongAnswers = null;
    }

    @Test
    public void createRandomModelNotNullCheck() {
        rightButtonCombinationModel = RightButtonCombinationModel.createRandomModel();
        assertNotNull(rightButtonCombinationModel);
    }

    @Test
    public void createRandomModelTest() {
        rightButton = new RightButtonModel(RightButton.RIGHT);
        secondRightButton = new RightButtonModel(RightButton.UP);

        wrongButton = new RightButtonModel(RightButton.LEFT);
        secondWrongButton = new RightButtonModel(RightButton.DOWN);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        rightButtonCombinationModel = RightButtonCombinationModel.createRandomModel();

        String challengeCheck = rightButtonCombinationModel.getChallenge();

        assertEquals(challengeCheck, rightButtonCombinationModel.challenge);
    }

}