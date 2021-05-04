package com.se2.bopit.domain;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModel<M extends ResponseModel> implements MiniGame {
    public String challenge;

    public M correctResponse;

    public List<M> responses;

    GameListener listener;

    @SafeVarargs
    public GameModel(String challenge, M correctResponse, M... wrongResponses) {
        this(challenge, correctResponse, Arrays.asList(wrongResponses));
    }

    public GameModel(String challenge, M correctResponse, List<M> wrongResponses) {
        this.challenge = challenge;
        this.correctResponse = correctResponse;
        this.responses = new ArrayList<>();
        responses.addAll(wrongResponses);
        correctResponse.isCorrect = true;
        responses.add(correctResponse);
        Collections.shuffle(responses);
    }

    protected boolean checkResponse(M response) {
        return correctResponse == response;
    }

    public void handleResponse(M response) {
        listener.onGameResult(
                checkResponse(response));
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

}
