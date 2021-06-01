package com.se2.bopit.domain.drawingminigame;

import android.graphics.RectF;
import com.se2.bopit.domain.GameModel;

public class DrawingGameModel extends GameModel<DrawingResponseModel> {

    private static final int ERROR = 150;

    @Override
    public boolean checkResponse(DrawingResponseModel response) {
        RectF bounds = new RectF();
        response.drawnPath.computeBounds(bounds, false);

        float heightDifference = response.solution.getHeight() - (bounds.bottom - bounds.top);
        float widthDifference = response.solution.getWidth() - (bounds.right - bounds.left);

        return Math.abs(heightDifference) <= ERROR && Math.abs(widthDifference) <= ERROR;
    }

    @Override
    public boolean handleResponse(Object response) {
        boolean result = checkResponse((DrawingResponseModel)response);
        if (result)
            listener.onGameResult(true);
        return result;
    }
}