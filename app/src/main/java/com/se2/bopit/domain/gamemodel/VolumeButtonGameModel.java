package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.VolumeButton;
import com.se2.bopit.domain.responsemodel.VolumeButtonModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeButtonGameModel extends MultipleChoiceGameModel<VolumeButtonModel> {

    protected VolumeButtonGameModel(String challenge, VolumeButtonModel correctResponse, List<VolumeButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    /**
     * Randomly picks an answers from a list of possible answers to initialize the correct response
     *
     * @return GameModel with the correct response and a "List" with the wrong answer
     */
    public static VolumeButtonGameModel createRandomModel() {

        List<VolumeButtonModel> possibleAnswers = Arrays.stream(VolumeButton.values())
                .map(VolumeButtonModel::new)
                .collect(Collectors.toList());

        Collections.shuffle(possibleAnswers);

        VolumeButtonModel correctResponse = possibleAnswers.get(0);

        List<VolumeButtonModel> wrongResponses = possibleAnswers.subList(1, possibleAnswers.size());

        return new VolumeButtonGameModel(
                String.format("Press volume %s", correctResponse.getLabel()),
                correctResponse,
                wrongResponses);

    }

    public String getChallenge() {
        return super.getChallenge();
    }
}
