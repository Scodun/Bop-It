package com.se2.bopit.domain.drawingminigame;

import android.graphics.Bitmap;
import android.graphics.Path;
import com.se2.bopit.domain.ResponseModel;

public class DrawingResponseModel extends ResponseModel {
    public final Bitmap solution;
    public final Path drawnPath;

    public DrawingResponseModel(Bitmap solution, Path drawnPath) {
        this.solution = solution;
        this.drawnPath = drawnPath;
    }
}
