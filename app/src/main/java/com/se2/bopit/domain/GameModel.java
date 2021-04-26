package com.se2.bopit.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModel<R extends ResponseModel> {
    public final String challenge;

    public final List<R> responses;

    public GameModel(String challenge, R correctResponse, R... wrongResponses) {
        this.challenge = challenge;
        this.responses = new ArrayList<>(wrongResponses.length + 1);
        responses.addAll(Arrays.asList(wrongResponses));
        correctResponse.isCorrect = true;
        responses.add(correctResponse);
        Collections.shuffle(responses);
    }
}
