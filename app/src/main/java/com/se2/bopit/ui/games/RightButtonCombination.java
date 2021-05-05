package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class RightButtonCombination extends Fragment implements MiniGame {

    GameListener listener;

    RightButtonCombination(){
        super();
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
