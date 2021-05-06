package com.se2.bopit.ui.games;

import android.graphics.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.DrawLineCanvas;

import java.util.ArrayList;
import java.util.Collections;

public class DrawingMinigame extends Fragment implements MiniGame {

    private final int ERROR = 100;

    private static final ArrayList<Integer> possibleAnswersResourceIds = initializeResourceIds();

    private GameListener listener;
    private static Bitmap solution;

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

        Collections.shuffle(possibleAnswersResourceIds);

        solution = BitmapFactory.decodeResource(getResources(), possibleAnswersResourceIds.get(0));

        layout.addView(new DrawLineCanvas(getContext(), this, solution));

        return view;
    }

    public void checkShape(Path touchPath) {
        //TODO find a better way to this
        RectF bounds = new RectF();
        touchPath.computeBounds(bounds, false);

        float heightDifference = solution.getHeight() - (bounds.bottom - bounds.top);
        float widthDifference = solution.getWidth() - (bounds.right - bounds.left);

        if (Math.abs(heightDifference) < ERROR && Math.abs(widthDifference) < ERROR)
            listener.onGameResult(true);
    }

    private static ArrayList<Integer> initializeResourceIds() {
        ArrayList<Integer> resourceIds = new ArrayList<>();

        resourceIds.add(R.drawable.circle);
        resourceIds.add(R.drawable.square);

        return resourceIds;
    }
}
