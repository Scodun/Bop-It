package com.se2.bopit.ui;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.se2.bopit.R;

public class DrawLineCanvas extends View {

    private Canvas canvas;

    private final Paint pathLinePaint;
    private Path touchPath;

    private Bitmap bitmap;

    public DrawLineCanvas(Context context) {
        super(context);

        pathLinePaint = new Paint();
        pathLinePaint.setColor(getResources().getColor(R.color.primary));
        pathLinePaint.setAntiAlias(true);
        pathLinePaint.setStyle(Paint.Style.STROKE);
        pathLinePaint.setStrokeWidth(15);

        touchPath = new Path();
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        bitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
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
                canvas.drawPath(touchPath, pathLinePaint);
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

        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(touchPath, pathLinePaint);
    }
}