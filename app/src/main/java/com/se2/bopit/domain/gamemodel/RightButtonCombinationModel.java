package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.RightButton;
import com.se2.bopit.domain.responsemodel.RightButtonModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RightButtonCombinationModel extends MultipleChoiceGameModel<RightButtonModel> {

    private RightButtonModel secondCorrectResponse;

    public RightButtonCombinationModel(String challenge, RightButtonModel correctResponse,
                                       RightButtonModel secondCorrectResponse, List<RightButtonModel> wrongAnswers) {
        super(challenge, correctResponse, wrongAnswers);
        this.setSecondCorrectResponse(secondCorrectResponse);
    }

    /**
     * Randomly picks two answers from a list of possible answers to initialize the first and the second correct response
     *
     * @return GameModel with two correct responses and a List with the wrong answers
     */

    public static RightButtonCombinationModel createRandomModel() {

        List<RightButtonModel> possibleAnswers = Arrays.stream(RightButton.values())
                .map(RightButtonModel::new)
                .collect(Collectors.toList());

        Collections.shuffle(possibleAnswers);

        RightButtonModel firstCorrectResponse = possibleAnswers.get(0);

        Collections.shuffle(possibleAnswers);

        RightButtonModel secondCorrectResponse = possibleAnswers.get(0);

        List<RightButtonModel> wrongResponses = new ArrayList<>();

        for (RightButtonModel rightButtonModel : possibleAnswers) {
            if (rightButtonModel != firstCorrectResponse && rightButtonModel != secondCorrectResponse) {
                wrongResponses.add(rightButtonModel);
            }
        }
        return new RightButtonCombinationModel(
                firstCorrectResponse.getLabel() + " " + secondCorrectResponse.getLabel(),
                firstCorrectResponse,
                secondCorrectResponse,
                wrongResponses
        );
    }

    public String getChallenge() {
        return super.getChallenge();
    }

    public RightButtonModel getSecondCorrectResponse() {
        return secondCorrectResponse;
    }

    public void setSecondCorrectResponse(RightButtonModel secondCorrectResponse) {
        this.secondCorrectResponse = secondCorrectResponse;
    }
}
