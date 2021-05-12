package com.se2.bopit.ui.games;

import com.se2.bopit.R;
import com.se2.bopit.domain.ButtonImage;
import com.se2.bopit.domain.RightButton;
import com.se2.bopit.domain.RightButtonCombinationModel;
import com.se2.bopit.domain.RightButtonModel;
import com.se2.bopit.domain.interfaces.GameListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RightButtonCombinationTest {

    RightButtonCombination rightButtonCombination;

    RightButtonCombinationModel gameModel;

    RightButtonModel rightButton;
    RightButtonModel secondRightButton;

    RightButtonModel wrongButton;
    RightButtonModel secondWrongButton;

    List<RightButtonModel> wrongAnswers;

    @Before
    public void setUp() throws Exception {
        rightButtonCombination = new RightButtonCombination();
        wrongAnswers = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        rightButtonCombination = null;
        wrongAnswers = null;
    }

    @Test
    public void initializeButtons() {
    }

    @Test
    public void firstButtonToClickIsRIGHT() {
        rightButton = new RightButtonModel(RightButton.RIGHT);
        secondRightButton = new RightButtonModel(RightButton.LEFT);

        wrongButton = new RightButtonModel(RightButton.DOWN);
        secondWrongButton = new RightButtonModel(RightButton.UP);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        gameModel = new RightButtonCombinationModel("RIGHT RIGHT",rightButton,secondRightButton,wrongAnswers);

        rightButtonCombination.rightButtonCombinationModel = gameModel;
        rightButtonCombination.count = 0;

        assertEquals(R.id.pressRight,rightButtonCombination.findButton());

    }
    @Test
    public void firstButtonToClickIsLEFT() {
        rightButton = new RightButtonModel(RightButton.LEFT);
        secondRightButton = new RightButtonModel(RightButton.LEFT);

        wrongButton = new RightButtonModel(RightButton.DOWN);
        secondWrongButton = new RightButtonModel(RightButton.UP);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        gameModel = new RightButtonCombinationModel("RIGHT RIGHT",rightButton,secondRightButton,wrongAnswers);

        rightButtonCombination.rightButtonCombinationModel = gameModel;
        rightButtonCombination.count = 0;

        assertEquals(R.id.pressLeft,rightButtonCombination.findButton());
    }
    @Test
    public void firstButtonToClickIsUP() {
        rightButton = new RightButtonModel(RightButton.UP);
        secondRightButton = new RightButtonModel(RightButton.LEFT);

        wrongButton = new RightButtonModel(RightButton.DOWN);
        secondWrongButton = new RightButtonModel(RightButton.UP);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        gameModel = new RightButtonCombinationModel("RIGHT RIGHT",rightButton,secondRightButton,wrongAnswers);

        rightButtonCombination.rightButtonCombinationModel = gameModel;
        rightButtonCombination.count = 0;

        assertEquals(R.id.pressUp,rightButtonCombination.findButton());
    }
    @Test
    public void firstButtonToClickIsDOWN() {
        rightButton = new RightButtonModel(RightButton.DOWN);
        secondRightButton = new RightButtonModel(RightButton.LEFT);

        wrongButton = new RightButtonModel(RightButton.DOWN);
        secondWrongButton = new RightButtonModel(RightButton.UP);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        gameModel = new RightButtonCombinationModel("RIGHT RIGHT",rightButton,secondRightButton,wrongAnswers);

        rightButtonCombination.rightButtonCombinationModel = gameModel;
        rightButtonCombination.count = 0;

        assertEquals(R.id.pressDown,rightButtonCombination.findButton());
    }
    @Test
    public void secondButtonToClickIsRIGHT() {
        rightButton = new RightButtonModel(RightButton.RIGHT);
        secondRightButton = new RightButtonModel(RightButton.RIGHT);

        wrongButton = new RightButtonModel(RightButton.DOWN);
        secondWrongButton = new RightButtonModel(RightButton.UP);

        wrongAnswers.add(wrongButton);
        wrongAnswers.add(secondWrongButton);

        gameModel = new RightButtonCombinationModel("RIGHT RIGHT",rightButton,secondRightButton,wrongAnswers);

        rightButtonCombination.rightButtonCombinationModel = gameModel;
        rightButtonCombination.count = 1;

        assertEquals(R.id.pressRight,rightButtonCombination.findButton());

    }
    @Test
    public void checkFirstClick() {
    }
    @Test
    public void checkClickTrue() {
        assertTrue(rightButtonCombination.checkClick(true,true));
    }
    @Test
    public void setSecondOnClickListener() {
    }
    @Test
    public void setGameListener(){
        GameListener listener = r -> {};
        rightButtonCombination.setGameListener(listener);
        assertSame(listener, rightButtonCombination.rightButtonCombinationModel.getGameListener());
    }
}