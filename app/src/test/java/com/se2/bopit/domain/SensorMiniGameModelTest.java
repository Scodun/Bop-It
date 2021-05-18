package com.se2.bopit.domain;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.SensorEventModelListener;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.function.LongConsumer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class SensorMiniGameModelTest {

    SensorMiniGameModel gameModel;
    SensorResponseModel expectedResponse;
    Sensor sensorMock;
    Context contextMock;
    GameListener gameListenerMock;
    PlatformFeaturesProvider platformProviderMock;

    @Before
    public void setUp() throws Exception {
        sensorMock = mock(Sensor.class);
        contextMock = mock(Context.class);
        expectedResponse = new SensorResponseModel(Sensor.TYPE_LIGHT, 1);
        gameModel = new SensorMiniGameModel(expectedResponse) {
            @Override
            protected boolean checkResponse(SensorResponseModel response) {
                return true;
            }
        };
        gameListenerMock = mock(GameListener.class);
        gameModel.setGameListener(gameListenerMock);
        platformProviderMock = mock(PlatformFeaturesProvider.class);
        gameModel.setPlatformFeaturesProvider(platformProviderMock);
    }

    @After
    public void tearDown() {
        reset(sensorMock, contextMock, platformProviderMock, gameListenerMock);
    }

    @Test
    public void onSensorChanged() {
        SensorEventModel eventModel = new SensorEventModel(0, Sensor.TYPE_LIGHT, 0, 10);
        gameModel.onSensorChanged(eventModel);
        verify(gameListenerMock).onGameResult(eq(true));
    }

    @Test
    public void onAccuracyChanged() {
        gameModel.onAccuracyChanged(1);
    }

    @Test
    public void resumeSensor() {
        gameModel.resumeSensor(contextMock);
        verify(platformProviderMock).registerSensorListener(eq(contextMock), eq(gameModel.sensorType), eq(gameModel));
    }

    @Test
    public void pauseSensorWithoutResume() {
        gameModel.pauseSensor();
        verifyNoInteractions(platformProviderMock);
    }

    @Test
    public void pauseSensorAfterResume() {
        gameModel.resumeSensor(contextMock);
        gameModel.pauseSensor();
        verify(platformProviderMock).unregisterSensorListener(eq(contextMock), eq(gameModel));
    }
}