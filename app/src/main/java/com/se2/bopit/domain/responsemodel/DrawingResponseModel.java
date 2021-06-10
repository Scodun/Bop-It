package com.se2.bopit.domain.responsemodel;

import android.graphics.Bitmap;
import android.graphics.Path;

public class DrawingResponseModel extends ResponseModel {
    public final Bitmap solution;
    public final Path drawnPath;

    public DrawingResponseModel(Bitmap solution, Path drawnPath) {
        this.solution = solution;
        this.drawnPath = drawnPath;
    }
}
