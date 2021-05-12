package com.se2.bopit.ui;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.se2.bopit.R;
import com.se2.bopit.ui.games.DrawingMinigame;

public class DrawTouchPathCanvas extends View {

    private final double SCALING_FACTOR = 0.55;

    private Paint pathLinePaint;
    private Path drawnPath;

    private Bitmap background;

    private Bitmap solution;
    private final DrawingMinigame minigame;

    public DrawTouchPathCanvas(Context context, Bitmap solution, DrawingMinigame minigame) {
        super(context);

        this.solution = solution;
        this.minigame = minigame;
        drawnPath = new Path();

        initializePathLinePaint();
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        background = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);

        solution = Bitmap.createScaledBitmap(
                solution,
                (int) (newWidth*SCALING_FACTOR),
                (int) (newWidth*SCALING_FACTOR),
                false
        );

        float drawAtX = (float) ((double) newWidth/2 - (double) solution.getWidth()/2);
        float drawAtY = (float) ((double) newHeight/2 - (double) solution.getHeight()/2);

        canvas.save();

        int randomAngle =(int)((Math.random() * 8) + 1) * 45;

        canvas.rotate(randomAngle, newWidth/2, newHeight/2);

        // Draw the middle of the solution in the middle of the new space
        canvas.drawBitmap(solution, drawAtX, drawAtY, null);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawnPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawnPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawnPath.lineTo(touchX, touchY);
                minigame.checkShape(solution, drawnPath);
                drawnPath = new Path();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawPath(drawnPath, pathLinePaint);
    }

    private void initializePathLinePaint() {
        pathLinePaint = new Paint();
        pathLinePaint.setColor(getResources().getColor(R.color.primary));
        pathLinePaint.setAntiAlias(true);
        pathLinePaint.setStyle(Paint.Style.STROKE);
        pathLinePaint.setStrokeWidth(20);
    }
}