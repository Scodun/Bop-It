package com.se2.bopit.ui.games;

import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class VolumeButtonMinigame extends Fragment implements MiniGame {
    GameListener listener;

    public VolumeButtonMinigame(){
        super(R.layout.fragment_volume_button_game);
        this.listener = null;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
}
