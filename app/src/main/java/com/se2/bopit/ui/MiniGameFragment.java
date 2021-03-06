package com.se2.bopit.ui;

import androidx.fragment.app.Fragment;

import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;

public class MiniGameFragment<M extends GameModel<?>> extends Fragment implements MiniGame {
    protected final String tag = getClass().getSimpleName();

    protected M gameModel;

    protected MiniGameFragment() {
    }

    protected MiniGameFragment(M gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void setGameListener(GameListener listener) {
        gameModel.setGameListener(listener);
    }

    @Override
    public void setPlatformFeaturesProvider(PlatformFeaturesProvider provider) {
        gameModel.setPlatformFeaturesProvider(provider);
    }

    @Override
    public GameModel<?> getModel() {
        return gameModel;
    }
}
