package com.se2.bopit.domain;

import android.hardware.SensorEvent;

public class SensorEventModel {
    public final int accuracy;
    public final int sensorType;
    public final long timestamp;
    public final float[] values;

    public SensorEventModel(int accuracy, int sensorType, long timestamp, float... values) {
        this.accuracy = accuracy;
        this.sensorType = sensorType;
        this.timestamp = timestamp;
        this.values = values;
    }

    public SensorEventModel(SensorEvent event) {
        this(event.accuracy, event.sensor.getType(), event.timestamp, event.values);
    }
}
