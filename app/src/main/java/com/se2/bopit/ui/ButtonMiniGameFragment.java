package com.se2.bopit.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public abstract class ButtonMiniGameFragment extends Fragment implements MiniGame {
    final String TAG = getClass().getSimpleName();

    GameListener gameListener;

    GameModel<ButtonModel> gameModel;

    public ButtonMiniGameFragment(GameModel<ButtonModel> gameModel) {
        super();
        this.gameModel = gameModel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText(gameModel.challenge);

        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        for(ButtonModel model : gameModel.responses) {
            Button button = (Button) inflater.inflate(
                    getButtonTemplate(model), layout, false);

            if(model.label != null) {
                button.setText(model.label);
            }
            // TODO dirty solution. consider responding with model and letting game engine decide
            button.setOnClickListener(model.isCorrect
                    ? this::handleCorrectResponse
                    : this::handleWrongResponse);
            layout.addView(button);
        }

        Log.d(TAG, "view created");

        return view;
    }

    int getButtonTemplate(ButtonModel model) {
        switch (model.color != null ? model.color : ButtonColor.DEFAULT) {
            case RED:
                return R.layout.button_red_template;
            case GREEN:
                return R.layout.button_green_template;
            case BLUE:
                return R.layout.button_blue_template;
            default:
                return R.layout.button_template;
        }
    }

    void handleWrongResponse(View view) {
        Log.d(TAG, "wrong response!");
        gameListener.onGameResult(false);
    }
    void handleCorrectResponse(View view) {
        Log.d(TAG, "correct response!");
        gameListener.onGameResult(true);
    }


    public GameListener getGameListener() {
        return gameListener;
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }
}

