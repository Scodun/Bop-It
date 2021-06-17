package com.se2.bopit.ui.games;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.se2.bopit.R;
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.GameModel;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.drawingminigame.DrawingGameModel;
import com.se2.bopit.ui.DifficultyActivity;
import com.se2.bopit.ui.DrawTouchPathCanvas;
import com.se2.bopit.ui.MiniGameFragment;

import java.util.ArrayList;
import java.util.Collections;

public class DrawingMinigame extends MiniGameFragment<DrawingGameModel> {

    private static final ArrayList<Integer> possibleAnswersResourceIds = initializeResourceIds();

    public DrawingMinigame() {
        super(new DrawingGameModel());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);
        LinearLayout layout = view.findViewById(R.id.buttonsRegion);

        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText("Trace the Shape");
        new TextToSpeech().sayText("Trace the Shape!", this.getContext());

        Collections.shuffle(possibleAnswersResourceIds);

        Bitmap solution = BitmapFactory.decodeResource(getResources(), possibleAnswersResourceIds.get(0));
        layout.addView(new DrawTouchPathCanvas(getContext(), solution, this));

        return view;
    }

    private static ArrayList<Integer> initializeResourceIds() {
        ArrayList<Integer> resourceIds = new ArrayList<>();

        resourceIds.add(R.drawable.circle);
        resourceIds.add(R.drawable.square);
        resourceIds.add(R.drawable.triangle);

        return resourceIds;
    }

    @Override
    public GameModel<?> getModel() {
        return null;
    }

    @Override
    public long getTime(Difficulty difficulty, int score) {
        double maxExponent = 8;
        double multiplier = 0.1;

        int base = 2000;
        switch (DifficultyActivity.difficulty) {
            case EASY:
                base = 2000;
            case MEDIUM:
                base = 1500;
            case HARD:
                base = 1000;
        }

        return generateTime(maxExponent, multiplier, base, score);
    }

}
