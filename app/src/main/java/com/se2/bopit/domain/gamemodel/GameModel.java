package com.se2.bopit.domain.gamemodel;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;
import com.se2.bopit.domain.responsemodel.ResponseModel;

public abstract class GameModel<M extends ResponseModel> implements MiniGame {
    protected GameListener listener;

    protected PlatformFeaturesProvider platformFeaturesProvider;

    public abstract boolean checkResponse(M response);

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
