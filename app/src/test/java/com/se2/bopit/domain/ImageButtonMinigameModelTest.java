package com.se2.bopit.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ImageButtonMinigameModelTest {

    ImageButtonMinigameModel imageButtonMinigameModel;
    ImageButtonMinigameModel imageButtonMinigameModelCheck;

    ImageButtonModel rightImage;
    ImageButtonModel rightImageCheck;
    ImageButtonModel wrongImage;
    ImageButtonModel secondwrongImage;

    List<ImageButtonModel> wrongAnswers;
    List<ImageButtonModel> wrongAnswersCheck;

    @Before
    public void setUp() throws Exception {
        wrongAnswers = new ArrayList<>();
        wrongAnswersCheck = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        wrongAnswers = null;
        wrongAnswersCheck = null;
    }

    @Test
    public void createRandomModelNotNullCheck() {
        imageButtonMinigameModel = ImageButtonMinigameModel.createRandomModel();

        assertNotNull(imageButtonMinigameModel);
    }
    @Test
    public void rightButtonIsCAT() {
        rightImage = new ImageButtonModel(ButtonImage.CAT);
        wrongImage = new ImageButtonModel(ButtonImage.DOG);
        secondwrongImage = new ImageButtonModel(ButtonImage.ELEPHANT);
        wrongAnswers.add(wrongImage);
        wrongAnswers.add(secondwrongImage);

        imageButtonMinigameModel = ImageButtonMinigameModel.createRandomModel();

        String challengeCheck = imageButtonMinigameModel.getChallenge();

        assertEquals(challengeCheck, imageButtonMinigameModel.challenge);
    }

    @Test
    public void shuffleResponses(){
    imageButtonMinigameModel = ImageButtonMinigameModel.createRandomModel();
    wrongAnswers = imageButtonMinigameModel.responses;
    rightImage = imageButtonMinigameModel.correctResponse;
    wrongAnswers.remove(rightImage);

    do{
        imageButtonMinigameModelCheck = ImageButtonMinigameModel.createRandomModel();
        wrongAnswersCheck = imageButtonMinigameModelCheck.responses;
        rightImageCheck = imageButtonMinigameModelCheck.correctResponse;
        wrongAnswersCheck.remove(rightImageCheck);
    }while(rightImage.label.equals(rightImageCheck.label));

    assertNotEquals(wrongAnswers,wrongAnswersCheck);
    }
}