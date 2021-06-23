package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.responsemodel.ResponseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class MultipleChoiceGameModel<M extends ResponseModel> extends GameModel<M> {
    private String challenge;

    private M correctResponse;

    private List<M> responses;


    @SafeVarargs
    protected MultipleChoiceGameModel(String challenge, M correctResponse, M... wrongResponses) {
        this(challenge, correctResponse, Arrays.asList(wrongResponses));
    }

    protected MultipleChoiceGameModel(String challenge, M correctResponse, List<M> wrongResponses) {
        this.setChallenge(challenge);
        this.setCorrectResponse(correctResponse);
        this.setResponses(new ArrayList<>());
        getResponses().addAll(wrongResponses);
        correctResponse.setCorrect(true);
        getResponses().add(correctResponse);
        Collections.shuffle(getResponses());
    }

    @Override
    public boolean checkResponse(M response) {
        return response.isCorrect();
    }

    @Override
    public boolean handleResponse(Object response) {
        boolean result = response != null && checkResponse((M) response);
        if (listener != null) {
            listener.onGameResult(result);
        }
        return result;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public M getCorrectResponse() {
        return correctResponse;
    }

    public void setCorrectResponse(M correctResponse) {
        this.correctResponse = correctResponse;
    }

    public List<M> getResponses() {
        return responses;
    }

    public void setResponses(List<M> responses) {
        this.responses = responses;
    }
}
