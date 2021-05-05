package com.se2.bopit.ui;

import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.ActionModel;
import com.se2.bopit.domain.ButtonColor;
import com.se2.bopit.domain.ButtonModel;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class ActionMiniGameFragment extends Fragment implements MiniGame {
    final String TAG = getClass().getSimpleName();

    GameListener gameListener;

    GameModel<ActionModel> gameModel;

    public ActionMiniGameFragment(GameModel<ActionModel> gameModel) {
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
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);

        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText(gameModel.challenge);

        ImageView action = view.findViewById(R.id.actionImage);
        action.setImageResource(R.drawable.arrow_up);

        Log.d(TAG, "view created");

        return view;
    }


    /**
     * Randomly picks a number of answers from a list of possible answers to create the GameModel
     *
     * @param possibleAnswers List of possible answers
     * @param numberAnswers Number of answers to randomly choose from the list.
     *                      One of them will be correct.
     * @return GameModel with 1 correct response and possibleAnswers-1 incorrect responses
     */
    protected static GameModel<ActionModel> createGameModel(List<ActionModel> possibleAnswers, int numberAnswers) {

        for (ActionModel actionModel : possibleAnswers) {
            possibleAnswers.add(actionModel.clone());
        }

        Collections.shuffle(possibleAnswers);

        ActionModel correctResponse = possibleAnswers.get(0);

        ArrayList<ActionModel> wrongResponses = new ArrayList<>();
        for (int i = 1; i < numberAnswers; i++) {
            ActionModel wrongResponse = possibleAnswers.get(i);
            wrongResponse.isCorrect = false;
            wrongResponses.add(wrongResponse);
        }

        return new GameModel<ActionModel>(
                String.format("Select %s!", correctResponse.action),
                correctResponse,
                wrongResponses
        );
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

