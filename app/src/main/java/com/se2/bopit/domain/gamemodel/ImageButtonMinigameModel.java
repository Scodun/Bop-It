package com.se2.bopit.domain.gamemodel;


import com.se2.bopit.domain.ButtonImage;
import com.se2.bopit.domain.responsemodel.ImageButtonModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageButtonMinigameModel extends MultipleChoiceGameModel<ImageButtonModel> {

    public ImageButtonMinigameModel(String challenge, ImageButtonModel correctResponse, List<ImageButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    /**
     * Randomly an answer from a list of possible answers to initialize the correct answer
     *
     * @return GameModel with 1 correct answer and possibleAnswers-1 wrong answers
     */
    public static ImageButtonMinigameModel createRandomModel() {

        List<ImageButtonModel> possibleAnswers = Arrays.stream(ButtonImage.values())
                .map(ImageButtonModel::new)
                .collect(Collectors.toList());

        Collections.shuffle(possibleAnswers);

        ImageButtonModel correctResponse = possibleAnswers.get(0);

        List<ImageButtonModel> wrongAnswers = possibleAnswers.subList(1, possibleAnswers.size());

        return new ImageButtonMinigameModel(
                String.format("Select the %s", correctResponse.getLabel()),
                correctResponse,
                wrongAnswers
        );
    }

    public String getChallenge() {
        return super.getChallenge();
    }
}
