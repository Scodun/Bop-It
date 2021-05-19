package com.se2.bopit.domain;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VolumeButtonGameModel extends MultipleChoiceGameModel<VolumeButtonModel> {

    protected VolumeButtonGameModel(String challenge, VolumeButtonModel correctResponse, List<VolumeButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static VolumeButtonGameModel createRandomModel() {

        List<VolumeButtonModel> possibleAnswers = Arrays.stream(VolumeButton.values())
                .map(VolumeButtonModel::new)
                .collect(Collectors.toList());

        Collections.shuffle(possibleAnswers);

        VolumeButtonModel correctResponse = possibleAnswers.get(0);

        List<VolumeButtonModel> wrongResponses = possibleAnswers.subList(1,possibleAnswers.size());

        return new VolumeButtonGameModel(
                String.format("Press volume %s", correctResponse.label),
                correctResponse,
                wrongResponses);

    }
    public String getChallenge(){
        return challenge;
    }
}
