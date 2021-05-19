package com.se2.bopit.domain.mock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccelerometerMock {
    private SensorEvent sensorEventMock;
    private Sensor sensorMock;
    private Field sensorField;
    private Field valuesField;

    public SensorEvent getAccelerometerMock(float[] values) throws NoSuchFieldException,
            IllegalAccessException {
        sensorEventMock = mock(SensorEvent.class);
        sensorField = SensorEvent.class.getField("sensor");
        sensorField.setAccessible(true);
        sensorMock = mock(Sensor.class);
        when(sensorMock.getType()).thenReturn(Sensor.TYPE_ACCELEROMETER);
        sensorField.set(sensorEventMock, sensorMock);
        valuesField = SensorEvent.class.getField("values");
        valuesField.set(sensorEventMock, values);
        return sensorEventMock;
    }
}
