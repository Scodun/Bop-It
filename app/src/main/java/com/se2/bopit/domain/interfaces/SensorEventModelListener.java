package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.SensorEventModel;

public interface SensorEventModelListener {
    void onSensorChanged(SensorEventModel sensorEvent);

    default void onAccuracyChanged(int value) {
        // optional
    }
}
