package com.se2.bopit.ui;

import android.graphics.drawable.Drawable;
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
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonMiniGameModel;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.Random;

public abstract class SensorMiniGameFragment extends Fragment implements MiniGame {
    protected final String tag = getClass().getSimpleName();

    ButtonMiniGameModel gameModel;

    protected SensorMiniGameFragment(ButtonMiniGameModel gameModel) {
        super();
        this.gameModel = gameModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText(gameModel.challenge);

        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        for(ButtonModel model : gameModel.responses) {
            layout.addView(applyButtonModel(model,
                    inflater.inflate(R.layout.button_template, layout, false)));
        }

        Log.d(tag, "view created");

        return view;
    }

    Button applyButtonModel(ButtonModel model, View buttonView) {
        Button button = (Button) buttonView;
        if(model.label != null) {
            button.setText(model.label);
        }

        setButtonColor(model, button);

        button.setOnClickListener(v -> gameModel.handleResponse(model));

        return button;
    }

    void setButtonColor(ButtonModel model, Button button) {
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, getTintFromButtonColor(model.color));
        button.setBackground(buttonDrawable);
    }

    private int getTintFromButtonColor(ButtonColor buttonColor) {
        switch (buttonColor) {
            case RED:
                return getResources().getColor(R.color.red);
            case GREEN:
                return getResources().getColor(R.color.green);
            case BLUE:
                return getResources().getColor(R.color.blue);
            case PURPLE:
                return getResources().getColor(R.color.purple);
            case YELLOW:
                return getResources().getColor(R.color.yellow);
            case ORANGE:
                return getResources().getColor(R.color.orange);
            case PINK:
                return getResources().getColor(R.color.pink);
            case BLACK:
                return getResources().getColor(R.color.black);
            case RANDOM:
                // Chooses a random Element from the Enum except the last which is Random so this will not run forever
                return getTintFromButtonColor(ButtonColor.values()[new Random().nextInt(ButtonColor.values().length - 1)]);
            case DEFAULT:
            default:
                return getResources().getColor(R.color.primary);

        }
    }

    public void setGameListener(GameListener listener) {
        gameModel.setGameListener(listener);
    }
}
