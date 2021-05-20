package com.se2.bopit.ui.games;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.DrawTouchPathCanvas;

import java.util.ArrayList;
import java.util.Collections;

public class DrawingMinigame extends Fragment implements MiniGame {

    private static final ArrayList<Integer> possibleAnswersResourceIds = initializeResourceIds();

    private GameListener listener;
    private static final int ERROR = 150;

    public DrawingMinigame() {
        super(R.layout.fragment_button_component);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
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

    public void checkShape(Bitmap solution, Path drawnPath) {
        RectF bounds = new RectF();
        drawnPath.computeBounds(bounds, false);

        float heightDifference = solution.getHeight() - (bounds.bottom - bounds.top);
        float widthDifference = solution.getWidth() - (bounds.right - bounds.left);

        if (Math.abs(heightDifference) < ERROR && Math.abs(widthDifference) < ERROR)
            listener.onGameResult(true);
    }

    private static ArrayList<Integer> initializeResourceIds() {
        ArrayList<Integer> resourceIds = new ArrayList<>();

        resourceIds.add(R.drawable.circle);
        resourceIds.add(R.drawable.square);
        resourceIds.add(R.drawable.triangle);

        return resourceIds;
    }
}
