package com.se2.bopit.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.se2.bopit.R;
import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.gamemodel.ButtonMiniGameModel;
import com.se2.bopit.domain.responsemodel.ButtonModel;

import java.util.Random;

import info.hoang8f.widget.FButton;

public abstract class ButtonMiniGameFragment extends MiniGameFragment<ButtonMiniGameModel> {
    protected ButtonMiniGameFragment(ButtonMiniGameModel gameModel) {
        super(gameModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText(gameModel.getChallenge());
        new TextToSpeech().sayText(gameModel.getChallenge().split(" ")[1], this.getContext());

        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        for (ButtonModel model : gameModel.getResponses()) {
            layout.addView(applyButtonModel(model,
                    inflater.inflate(R.layout.button_template, layout, false)));
        }

        Log.d(tag, "view created");

        return view;
    }

    FButton applyButtonModel(ButtonModel model, View buttonView) {
        FButton button = (FButton) buttonView;
        if (model.label != null) {
            button.setText(model.label);
        }

        setButtonColor(model, button);

        button.setOnClickListener(v -> gameModel.handleResponse(model));

        return button;
    }

    void setButtonColor(ButtonModel model, FButton button) {
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, getTintFromButtonColor(model.color));
        button.setBackground(buttonDrawable);

        button.setButtonColor(getTintFromButtonColor(model.color));
    }

    private int getTintFromButtonColor(ButtonColor buttonColor) {
        switch (buttonColor) {
            case RED:
                return ContextCompat.getColor(this.requireContext(), R.color.red);
            case GREEN:
                return ContextCompat.getColor(this.requireContext(), R.color.green);
            case BLUE:
                return ContextCompat.getColor(this.requireContext(), R.color.blue);
            case PURPLE:
                return ContextCompat.getColor(this.requireContext(), R.color.purple);
            case YELLOW:
                return ContextCompat.getColor(this.requireContext(), R.color.yellow);
            case ORANGE:
                return ContextCompat.getColor(this.requireContext(), R.color.orange);
            case PINK:
                return ContextCompat.getColor(this.requireContext(), R.color.pink);
            case BLACK:
                return ContextCompat.getColor(this.requireContext(), R.color.black);
            case RANDOM:
                // Chooses a random Element from the Enum except the last which is Random so this will not run forever
                return getTintFromButtonColor(ButtonColor.values()[new Random().nextInt(ButtonColor.values().length - 1)]);
            case DEFAULT:
            default:
                return ContextCompat.getColor(this.requireContext(), R.color.primary);
        }
    }
}

