package com.se2.bopit.ui;

import com.se2.bopit.domain.gamemodel.SensorMiniGameModel;

public abstract class SensorMiniGameFragment<M extends SensorMiniGameModel> extends MiniGameFragment<M> {
    protected SensorMiniGameFragment(M gameModel) {
        super(gameModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameModel.resumeSensor(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        gameModel.pauseSensor();
    }
}
