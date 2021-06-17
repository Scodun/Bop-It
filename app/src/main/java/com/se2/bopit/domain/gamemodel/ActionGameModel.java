package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.responsemodel.ResponseModel;

public abstract class ActionGameModel<R extends ResponseModel> extends GameModel<R> {
    protected R expectedResponse;

    protected ActionGameModel(R expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    @Override
    public boolean handleResponse(Object response) {
        boolean result = response != null && checkResponse((R)response);
        if (result && listener != null) {
            listener.onGameResult(true);
        }
        return result;
    }
}
