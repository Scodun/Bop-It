package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.gamemodel.VolumeButtonGameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import pl.droidsonroids.gif.GifImageView;

public class VolumeButtonMinigame extends Fragment implements MiniGame {

    VolumeButtonGameModel gameModel;
    GifImageView gifImageView;
    TextView textView;
    String text;
    Boolean isCorrect;

    public VolumeButtonMinigame() {
        super(R.layout.fragment_volume_button_game);
        gameModel = VolumeButtonGameModel.createRandomModel();
    }

    @Override
    public void setGameListener(GameListener listener) {
        gameModel.setGameListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(
                (View v, int keyCode, KeyEvent event) -> {
                    checkPressedKey();
                    if (keyCode == getKeyEvent()) {
                        isCorrect = true;
                        return true;
                    } else {
                        isCorrect = false;
                        return false;
                    }
                });

        gifImageView = view.findViewById(R.id.gifImageView);

        text = gameModel.getChallenge();
        textView = view.findViewById(R.id.VolumeButtonMessage);
        textView.setText(text);
        new TextToSpeech().sayText("Volume" + text.split(" ")[2], this.getContext());
        if (text.equals("Press volume DOWN")) {
            setGif();
        }

    }

    /**
     * Sets ImageResource to volume_down, if the user has to "reduce" the volume
     */
    public void setGif() {
        gifImageView.setImageResource(R.drawable.volume_down);
    }

    /**
     * Checks if the right button was pressed or not
     */
    public void checkPressedKey() {
        GameListener listener = gameModel.getGameListener();
        if (isCorrect != null && listener != null) {
            listener.onGameResult(isCorrect);
        }
    }

    /**
     * Chooses the KeyEvent depending on the correctResponse of gameModel
     *
     * @return the KeyEvent which is needed to accomplish the challenge
     */
    public int getKeyEvent() {
        switch (gameModel.getCorrectResponse().getVolumeButton()) {
            case DOWN:
                return KeyEvent.KEYCODE_VOLUME_DOWN;
            case UP:
                return KeyEvent.KEYCODE_VOLUME_UP;
            default:
                Log.e("ImageButtonMinigame", "Unknown Image");
                return 0;
        }
    }

    @Override
    public GameModel<?> getModel() {
        return gameModel;
    }

}
