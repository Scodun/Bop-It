package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.gamemodel.ImageButtonMinigameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.DifficultyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.se2.bopit.domain.Difficulty.EASY;
import static com.se2.bopit.domain.Difficulty.HARD;

public class ImageButtonMinigame extends Fragment implements MiniGame {

    ImageButtonMinigameModel imageButtonMinigameModel;

    List<ImageButton> imageButtonList;

    String text;
    TextView textView;

    public ImageButtonMinigame() {
        super(R.layout.fragment_image_button_game);
        imageButtonMinigameModel = ImageButtonMinigameModel.createRandomModel();
    }

    @Override
    public void setGameListener(GameListener listener) {
        imageButtonMinigameModel.setGameListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeButtons(view);

        LinearLayout layout = view.findViewById(R.id.linearLayout);

        imageButtonList = initializeImageButtonList(view);

        Collections.shuffle(imageButtonList);

        layout.removeAllViewsInLayout();

        for (ImageButton imageButton : imageButtonList) {
            layout.addView(imageButton);
        }

        text = imageButtonMinigameModel.getChallenge();
        textView = view.findViewById(R.id.textView);
        textView.setText(text);
        new TextToSpeech().sayText(text.split(" ")[2], this.getContext());
    }

    public void initializeButtons(View view) {
        view.findViewById(R.id.imageButton).setOnClickListener(clickHandler);
        view.findViewById(R.id.imageButton).setBackgroundColor(View.INVISIBLE);

        view.findViewById(R.id.imageButton2).setOnClickListener(clickHandler);
        view.findViewById(R.id.imageButton2).setBackgroundColor(View.INVISIBLE);

        view.findViewById(R.id.imageButton3).setOnClickListener(clickHandler);
        view.findViewById(R.id.imageButton3).setBackgroundColor(View.INVISIBLE);
    }

    /**
     * Adds all ImageButtons to a List
     *
     * @return List with all ImageButtons
     */
    public List<ImageButton> initializeImageButtonList(View view) {
        List<ImageButton> initializedList = new ArrayList<>();
        initializedList.add(view.findViewById(R.id.imageButton));
        initializedList.add(view.findViewById(R.id.imageButton2));
        initializedList.add(view.findViewById(R.id.imageButton3));
        return initializedList;
    }

    private final View.OnClickListener clickHandler = pressedButton -> {
        GameListener listener = imageButtonMinigameModel.getGameListener();
        if (textView.getText().toString().equals(text) && listener != null) {
            listener.onGameResult(pressedButton.getId() == findRightButton());
            setBackground(findRightButton());
        }
    };

    /**
     * Changes the Background of the right ImageButton green
     *
     * @param buttonToSetBackground - id of the ImageButton from which the background should be changed
     */
    void setBackground(int buttonToSetBackground) {
        getView().findViewById(buttonToSetBackground)
                .setBackgroundColor(View.VISIBLE);
        getView().findViewById(buttonToSetBackground)
                .setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.pressed_button_green, null));
    }

    /**
     * Finds the right id of the ImageButton which should be clicked
     *
     * @return id of an ImageButton
     */
    public int findRightButton() {
        switch (imageButtonMinigameModel.getCorrectResponse().getImage()) {
            case CAT:
                return R.id.imageButton;
            case DOG:
                return R.id.imageButton2;
            case ELEPHANT:
                return R.id.imageButton3;
            default:
                Log.e("ImageButtonMinigame", "Unknown Image");
                return 0;
        }
    }

    @Override
    public GameModel<?> getModel() {
        return imageButtonMinigameModel;
    }

    @Override
    public long getTime(Difficulty difficulty, int score) {
        if (DifficultyActivity.getDifficulty() == EASY)
            return generateTime(6.9, 0.07, 1600, score);
        else if (DifficultyActivity.getDifficulty() == HARD)
            return generateTime(6.9, 0.07, 800, score);
        else
            return generateTime(6.9, 0.07, 1200, score);
    }
}
