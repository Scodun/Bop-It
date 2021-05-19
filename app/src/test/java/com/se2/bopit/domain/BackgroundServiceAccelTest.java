package com.se2.bopit.domain;

import android.content.Intent;
import android.hardware.SensorEvent;

import com.se2.bopit.domain.mock.AccelerometerMock;
import com.se2.bopit.domain.services.BackgroundServiceAccelerometer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BackgroundServiceAccelTest {
    public static final String SHAKE_ACTION = "com.se2.bopit.ui.games.SHAKE";
    public static final String SHAKED = "isShaking";

    BackgroundServiceAccelerometer backgroundServiceAccelerometer;
    AccelerometerMock accelMock;
    float[] values;
    SensorEvent sensorEventMock;
    Intent intentMock;

    @Before
    public void setUp() throws Exception {
        backgroundServiceAccelerometer = new BackgroundServiceAccelerometer();
        accelMock = new AccelerometerMock();
        values = new float[3];
        intentMock = mock(Intent.class);
    }

    @Test
    public void testOnSensorChangedMore2() throws NoSuchFieldException, IllegalAccessException {
        values[0] = 7.0f;
        values[1] = 9.0f;
        values[2] = 8.0f;
        sensorEventMock = accelMock.getAccelerometerMock(values);
        backgroundServiceAccelerometer.onSensorChanged(sensorEventMock);
        assertTrue(backgroundServiceAccelerometer.isShaked());
    }

    @Test
    public void testOnSensorChangedLess2() throws NoSuchFieldException, IllegalAccessException {
        values[0] = 0.0f;
        values[1] = 0.0f;
        values[2] = 0.0f;
        sensorEventMock = accelMock.getAccelerometerMock(values);
        backgroundServiceAccelerometer.onSensorChanged(sensorEventMock);
        assertFalse(backgroundServiceAccelerometer.isShaked());
    }

    @After
    public void tearDown() {

    }
}
