package com.se2.bopit.domain;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageButtonMinigameModel extends GameModel<ImageButtonModel>{

    public ImageButtonMinigameModel(String challenge, ImageButtonModel correctResponse, List<ImageButtonModel> wrongResponses) {
        super(challenge, correctResponse, wrongResponses);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ImageButtonMinigameModel createRandomModel(){

        List<ImageButtonModel> possibleAnswers = Arrays.stream(ButtonImage.values())
                .map(ImageButtonModel::new)
                .collect(Collectors.toList());

        Collections.shuffle(possibleAnswers);

        ImageButtonModel correctResponse = possibleAnswers.get(0);

        List<ImageButtonModel> wrongAnswers = possibleAnswers.subList(1,possibleAnswers.size());

        return new ImageButtonMinigameModel(
                String.format("Select the %s", correctResponse.label),
                correctResponse,
                wrongAnswers
        );
    }
    public String getChallenge(){
        return challenge;
    }
}
