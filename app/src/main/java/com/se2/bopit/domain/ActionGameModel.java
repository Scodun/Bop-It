package com.se2.bopit.domain;

public abstract class ActionGameModel<R extends ResponseModel> extends GameModel<R> {
    protected R expectedResponse;

    protected ActionGameModel(R expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    @Override
    public boolean handleResponse(R response) {
        boolean result = checkResponse(response);
        if(result) {
            listener.onGameResult(true);
        }
        return result;
    }
}
