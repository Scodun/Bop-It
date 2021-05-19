package com.se2.bopit.ui.games;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.VolumeButtonGameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import pl.droidsonroids.gif.GifImageView;

public class VolumeButtonMinigame extends Fragment implements MiniGame {

    VolumeButtonGameModel gameModel;
    GifImageView gifImageView;
    TextView textView;
    String text;
    Boolean isCorrect;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public VolumeButtonMinigame(){
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
        view.setOnKeyListener(pressedKey);

        gifImageView = getView().findViewById(R.id.gifImageView);

        text = gameModel.challenge;
        textView = view.findViewById(R.id.VolumeButtonMessage);
        textView.setText(text);
        if(text.equals("Press volume DOWN")){
            setGif();
        }

    }
    public void setGif(){
        gifImageView.setImageResource(R.drawable.volume_down);
    }

    public final View.OnKeyListener pressedKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            checkPressedKey();
            if(keyCode == getKeyEvent()){
                isCorrect = true;
                return true;
            }else{
                isCorrect = false;
                return false;
            }
        }
    };
    public void checkPressedKey() {
        if (isCorrect != null) {
            gameModel.getGameListener().onGameResult(isCorrect);
        }
    }

    public int getKeyEvent(){
        switch(gameModel.correctResponse.volumeButton){
            case DOWN:
                return KeyEvent.KEYCODE_VOLUME_DOWN;
            case UP:
                return KeyEvent.KEYCODE_VOLUME_UP;
            default:
                Log.e("ImageButtonMinigame","Unknown Image");
                return 0;
        }
    }
}
