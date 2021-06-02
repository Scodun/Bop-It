package com.se2.bopit.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class MultipleChoiceGameModel<M extends ResponseModel> extends GameModel<M> {
    public String challenge;

    public transient M correctResponse;

    public List<M> responses;


    @SafeVarargs
    protected MultipleChoiceGameModel(String challenge, M correctResponse, M... wrongResponses) {
        this(challenge, correctResponse, Arrays.asList(wrongResponses));
    }

    protected MultipleChoiceGameModel(String challenge, M correctResponse, List<M> wrongResponses) {
        this.challenge = challenge;
        this.correctResponse = correctResponse;
        this.responses = new ArrayList<>();
        responses.addAll(wrongResponses);
        correctResponse.isCorrect = true;
        responses.add(correctResponse);
        Collections.shuffle(responses);
    }

    @Override
    protected boolean checkResponse(M response) {
        return response.isCorrect;
    }

    @Override
    public boolean handleResponse(Object response) {
        boolean result = response != null && checkResponse((M)response);
        if(listener != null) {
            listener.onGameResult(result);
        }
        return result;
    }
}
