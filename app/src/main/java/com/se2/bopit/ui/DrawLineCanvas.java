package com.se2.bopit.ui;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.se2.bopit.R;
import com.se2.bopit.ui.games.DrawingMinigame;

public class DrawLineCanvas extends View {

    private final Paint pathLinePaint;
    private Path touchPath;

    private Bitmap background;

    private final Bitmap solution;

    public DrawLineCanvas(Context context, Bitmap solution) {
        super(context);

        this.solution = solution;

        touchPath = new Path();

        pathLinePaint = new Paint();
        pathLinePaint.setColor(getResources().getColor(R.color.primary));
        pathLinePaint.setAntiAlias(true);
        pathLinePaint.setStyle(Paint.Style.STROKE);
        pathLinePaint.setStrokeWidth(20);
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        background = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);

        // Draw the middle of the solution in the middle of the new space
        canvas.drawBitmap(
                solution,
                (float) (newWidth/2-solution.getWidth()/2),
                (float) (newHeight/2-solution.getHeight()/2),
                null
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                touchPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                touchPath.lineTo(touchX, touchY);
                DrawingMinigame.checkShape(touchPath);
                touchPath = new Path();
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
        canvas.drawPath(touchPath, pathLinePaint);
    }
}