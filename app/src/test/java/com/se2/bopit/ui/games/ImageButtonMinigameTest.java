package com.se2.bopit.ui.games;

import android.util.Log;
import com.se2.bopit.R;
import com.se2.bopit.domain.ButtonImage;
import com.se2.bopit.domain.gamemodel.ImageButtonMinigameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.responsemodel.ImageButtonModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class ImageButtonMinigameTest {

    ImageButtonMinigame imageButtonMinigame;

    ImageButtonMinigameModel gameModelTest;

    ImageButtonModel rightImage;
    ImageButtonModel wrongImage;
    ImageButtonModel secondWrongImage;

    List<ImageButtonModel> wrongAnswers;

    Log log;

    @Before
    public void setUp() throws Exception {
        imageButtonMinigame = new ImageButtonMinigame();
        rightImage = new ImageButtonModel(ButtonImage.CAT);
        wrongImage = new ImageButtonModel(ButtonImage.ELEPHANT);
        secondWrongImage = new ImageButtonModel(ButtonImage.DOG);

        wrongAnswers = new ArrayList<>();
        wrongAnswers.add(wrongImage);
        wrongAnswers.add(secondWrongImage);

        log = mock(Log.class);
    }

    @After
    public void tearDown() throws Exception {
        imageButtonMinigame = null;
    }

    @Test
    public void rightButtonIsImageButton() {
        gameModelTest = new ImageButtonMinigameModel("Select the CAT", rightImage, wrongAnswers);

        imageButtonMinigame.imageButtonMinigameModel = gameModelTest;
        assertEquals(R.id.imageButton, imageButtonMinigame.findRightButton());
    }

    @Test
    public void rightButtonIsImageButton2() {
        rightImage.image = ButtonImage.DOG;
        gameModelTest = new ImageButtonMinigameModel("Select the DOG", rightImage, wrongAnswers);

        imageButtonMinigame.imageButtonMinigameModel = gameModelTest;
        assertEquals(R.id.imageButton2, imageButtonMinigame.findRightButton());
    }

    @Test
    public void rightButtonIsImageButton3() {
        rightImage.image = ButtonImage.ELEPHANT;
        gameModelTest = new ImageButtonMinigameModel("Select the ELEPHANT", rightImage, wrongAnswers);

        imageButtonMinigame.imageButtonMinigameModel = gameModelTest;
        assertEquals(R.id.imageButton3, imageButtonMinigame.findRightButton());
    }

    @Test
    public void setGameListenerTest() {
        GameListener listener = r -> {
        };
        imageButtonMinigame.setGameListener(listener);
        assertSame(listener, imageButtonMinigame.imageButtonMinigameModel.getGameListener());
    }
}