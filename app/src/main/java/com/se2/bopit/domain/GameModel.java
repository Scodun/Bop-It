package com.se2.bopit.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModel<M extends ResponseModel> {
    public String challenge;

    public List<M> responses;

    @SafeVarargs
    public GameModel(String challenge, M correctResponse, M... wrongResponses) {
        this(challenge, correctResponse, Arrays.asList(wrongResponses));
    }

    public GameModel(String challenge, M correctResponse, List<M> wrongResponses) {
        this.challenge = challenge;
        this.responses = new ArrayList<>();
        responses.addAll(wrongResponses);
        correctResponse.isCorrect = true;
        responses.add(correctResponse);
        Collections.shuffle(responses);
    }
}
