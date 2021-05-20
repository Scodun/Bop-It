package com.se2.bopit.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.se2.bopit.R;
import com.se2.bopit.ui.games.DrawingMinigame;

import java.util.Random;

public class DrawTouchPathCanvas extends View {

    private static final Random random = new Random();

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

        scaleSolution(newWidth);

        canvas.rotate(getRandomAngle(), newWidth / 2f, newHeight / 2f);

        float drawAtX = newWidth / 2f - solution.getWidth() / 2f;
        float drawAtY = newHeight / 2f - solution.getHeight() / 2f;

        canvas.drawBitmap(solution, drawAtX, drawAtY, null);
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
        pathLinePaint.setStrokeWidth(25);
    }

    private int getRandomAngle() {
        return (random.nextInt(8) + 1) * 45;
    }

    private void scaleSolution(int width) {
        double scalingFactor = 0.55;
        solution = Bitmap.createScaledBitmap(
                solution,
                (int) (width * scalingFactor),
                (int) (width * scalingFactor),
                false
        );
    }
}