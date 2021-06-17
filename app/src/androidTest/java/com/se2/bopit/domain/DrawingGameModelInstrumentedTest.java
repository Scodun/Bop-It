package com.se2.bopit.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.se2.bopit.domain.gamemodel.DrawingGameModel;
import com.se2.bopit.domain.mock.GameListenerMock;
import com.se2.bopit.domain.responsemodel.DrawingResponseModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DrawingGameModelInstrumentedTest {

    int bitmapSize = 1000;
    int error = 150;
    Context appContext;
    DrawingGameModel model;

    @Before
    public void setup() {
        model = new DrawingGameModel();
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testCheckResponseExactPath() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize));

        assertTrue(model.checkResponse(response));
    }

    @Test
    public void testCheckResponseMaxPath() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize + error));

        assertTrue(model.checkResponse(response));
    }

    @Test
    public void testCheckPathMinPath() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize-error));

        assertTrue(model.checkResponse(response));
    }

    @Test
    public void testCheckPathTooSmallPath() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize-error-1));

        assertFalse(model.checkResponse(response));
    }

    @Test
    public void testCheckPathTooBigPath() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize+error+1));

        assertFalse(model.checkResponse(response));
    }

    @Test
    public void testHandleResponseCorrect() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize));

        GameListenerMock listener = new GameListenerMock();

        model.setGameListener(listener);

        model.handleResponse(response);

        assertTrue(listener.result);
    }

    @Test
    public void testHandleResponseIncorrect() {
        Bitmap square = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.RGB_565);

        DrawingResponseModel response = new DrawingResponseModel(square, getSquarePath(bitmapSize-error-1));

        GameListenerMock listener = new GameListenerMock();

        model.setGameListener(listener);

        model.handleResponse(response);

        assertFalse(listener.result);
    }

    private Path getSquarePath(int sideLength) {
        return getRectPath(sideLength, sideLength);
    }

    private Path getRectPath(int height, int width) {
        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(0,height);
        path.lineTo(width,height);
        path.lineTo(width,0);
        path.lineTo(0,0);

        return path;
    }

}
