package com.se2.bopit.platform;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import com.se2.bopit.domain.SensorEventModel;
import com.se2.bopit.domain.interfaces.SensorEventModelListener;

import java.util.Arrays;
import java.util.WeakHashMap;

public class SensorEventListenerWrapper implements SensorEventListener {
    static final String TAG = "SensorEventListener";

    private static final WeakHashMap<SensorEventModelListener, SensorEventListenerWrapper> cache
            = new WeakHashMap<>();

    private final SensorEventModelListener delegate;

    private SensorEventListenerWrapper(SensorEventModelListener delegate) {
        this.delegate = delegate;
    }

    public static SensorEventListenerWrapper wrap(SensorEventModelListener delegate) {
        return cache.computeIfAbsent(delegate, SensorEventListenerWrapper::new);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged: @" + sensorEvent.timestamp
                + " (" + sensorEvent.accuracy + "): " + Arrays.toString(sensorEvent.values));
        delegate.onSensorChanged(new SensorEventModel(sensorEvent));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged: " + i);
        delegate.onAccuracyChanged(i);
    }
}
