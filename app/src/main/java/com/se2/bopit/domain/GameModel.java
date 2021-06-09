package com.se2.bopit.domain;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

public abstract class GameModel<M extends ResponseModel> implements MiniGame {
    protected transient GameListener listener;

    protected transient PlatformFeaturesProvider platformFeaturesProvider;

    protected abstract boolean checkResponse(M response);

    public abstract boolean handleResponse(Object response);

    public GameListener getGameListener() {
        return listener;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPlatformFeaturesProvider(PlatformFeaturesProvider platformFeaturesProvider) {
        this.platformFeaturesProvider = platformFeaturesProvider;
    }

    @Override
    public GameModel<M> getModel() {
        return this;
    }

}
