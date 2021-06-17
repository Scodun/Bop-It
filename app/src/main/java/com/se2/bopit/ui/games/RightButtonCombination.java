package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.RightButton;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.gamemodel.RightButtonCombinationModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class RightButtonCombination extends Fragment implements MiniGame {

    RightButtonCombinationModel rightButtonCombinationModel;

    String text;
    TextView textView;

    boolean firstClick = false;
    boolean secondClick = false;
    boolean result = false;

    int count;

    public RightButtonCombination() {
        super(R.layout.fragment_right_button_combination_game);
        rightButtonCombinationModel = RightButtonCombinationModel.createRandomModel();
    }

    @Override
    public void setGameListener(GameListener listener) {
        rightButtonCombinationModel.setGameListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeButtons();

        text = rightButtonCombinationModel.challenge;
        textView = getView().findViewById(R.id.textView2);
        textView.setText(text);
        new TextToSpeech().sayText(text.split(" ")[0], this.getContext());
        new TextToSpeech().sayText(text.split(" ")[1], this.getContext());
    }

    public void initializeButtons() {
        getView().findViewById(R.id.pressDown).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressRight).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressLeft).setOnClickListener(clickHandler);
        getView().findViewById(R.id.pressUp).setOnClickListener(clickHandler);
    }

    public final View.OnClickListener clickHandler = firstClickedButton -> {

        if (text.equals(rightButtonCombinationModel.challenge)) {
            firstClick = firstClickedButton.getId() == findButton();
            count++;
            checkFirstClick();
        }

    };

    /**
     * Finds the ImageButton which should be clicked as first or as second, depending on the result of chooseRightButton()
     *
     * @return - id of the right Button
     */
    int findButton() {
        switch (chooseRightButton()) {
            case RIGHT:
                return R.id.pressRight;
            case LEFT:
                return R.id.pressLeft;
            case UP:
                return R.id.pressUp;
            case DOWN:
                return R.id.pressDown;
            default:
                Log.e("RightButtonCombination", "Unknown Button");
                return 0;
        }
    }

    /**
     * Checks if the first click were true or false
     */

    void checkFirstClick() {
        GameListener listener = rightButtonCombinationModel.getGameListener();
        if (!firstClick && listener != null) {
            listener.onGameResult(result);
        } else {
            setSecondOnClickListener();
        }
    }

    /**
     * Checks if both clicks were right
     *
     * @param click1 - true -> first click was right
     * @param click2 - true -> second click was right, false -> second click was false
     * @return true -> if both clicks were right, false -> if the second click was false
     */

    public boolean checkClick(boolean click1, boolean click2) {
        return click1 && click2;
    }


    void setSecondOnClickListener() {
        GameListener listener = rightButtonCombinationModel.getGameListener();
        if (listener != null) {
            getView().findViewById(findButton()).setOnClickListener(clickedButton -> {
                secondClick = clickedButton.getId() == findButton();
                result = checkClick(firstClick, secondClick);
                listener.onGameResult(result);
            });
        }
    }

    /**
     * Chooses the RightButton depending on the value of count (either firstCorrectResponse or secondCorrectResponse)
     *
     * @return the right RightImage Object
     */
    RightButton chooseRightButton() {
        if (count == 0) {
            return rightButtonCombinationModel.correctResponse.rightButton;
        } else {
            return rightButtonCombinationModel.secondCorrectResponse.rightButton;
        }
    }

    @Override
    public GameModel<?> getModel() {
        return rightButtonCombinationModel;
    }

}
