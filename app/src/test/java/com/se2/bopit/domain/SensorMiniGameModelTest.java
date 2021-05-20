package com.se2.bopit.domain;

import android.content.Context;
import android.hardware.Sensor;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class SensorMiniGameModelTest {

    TestSensorMiniGameModel gameModel;
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
        gameModel = new TestSensorMiniGameModel(expectedResponse);
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
    public void onSensorChangedFalse() {
        SensorEventModel eventModel = new SensorEventModel(0, Sensor.TYPE_LIGHT, 0, 10);
        gameModel.onSensorChanged(eventModel);
        verifyNoInteractions(gameListenerMock);
    }

    @Test
    public void onSensorChangedTrue() {
        SensorEventModel eventModel = new SensorEventModel(0, Sensor.TYPE_LIGHT, 0, 0);
        gameModel.onSensorChanged(eventModel);
        verify(gameListenerMock).onGameResult(eq(true));
    }

    @Test
    public void onAccuracyChanged() {
        gameModel.onAccuracyChanged(1);

        SensorEventModel eventModel = new SensorEventModel(1, Sensor.TYPE_LIGHT, 0, 10);
        gameModel.onSensorChanged(eventModel);

        assertFalse(gameModel.history.isEmpty());

        gameModel.onAccuracyChanged(2);
        assertTrue(gameModel.history.isEmpty());
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

class TestSensorMiniGameModel extends SensorMiniGameModel {
    final LinkedList<SensorResponseModel> history = new LinkedList<>();

    public TestSensorMiniGameModel(SensorResponseModel expected) {
        super(expected);
    }

    @Override
    protected boolean checkResponse(SensorResponseModel response) {
        history.add(response);
        return response.values[0] < expectedResponse.values[0];
    }

    @Override
    public void onAccuracyChanged(int value) {
        history.clear();
    }
}
