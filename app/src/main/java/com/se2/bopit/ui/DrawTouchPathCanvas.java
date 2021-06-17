package com.se2.bopit.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import com.se2.bopit.R;
import com.se2.bopit.domain.drawingminigame.DrawingResponseModel;
import com.se2.bopit.ui.games.DrawingMinigame;

import java.util.Random;

public class DrawTouchPathCanvas extends View {

    private static final Random random = new Random();

    private final Paint pathLinePaint;
    private Path drawnPath;

    private Bitmap background;

    private Bitmap solution;
    private final DrawingMinigame minigame;

    public DrawTouchPathCanvas(Context context, Bitmap solution, DrawingMinigame minigame) {
        super(context);

        this.solution = solution;
        this.minigame = minigame;
        drawnPath = new Path();

        pathLinePaint = getPathLinePaint();
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
                DrawingResponseModel response = new DrawingResponseModel(solution, drawnPath);
                minigame.gameModel.handleResponse(response);
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

    private Paint getPathLinePaint() {
        Paint paint = new Paint();
        paint.setColor(getRandomColor());
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(25);
        return paint;
    }

    private int getRandomColor() {
        int n = random.nextInt(7);

        switch (n) {
            case 0:
                return getResources().getColor(R.color.red);
            case 1:
                return getResources().getColor(R.color.green);
            case 2:
                return getResources().getColor(R.color.blue);
            case 3:
                return getResources().getColor(R.color.purple);
            case 4:
                return getResources().getColor(R.color.yellow);
            case 5:
                return getResources().getColor(R.color.orange);
            case 6:
                return getResources().getColor(R.color.pink);
            default:
                return getResources().getColor(R.color.primary);
        }
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