package com.se2.bopit.domain;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorMiniGameModel extends ActionGameModel<SensorResponseModel> implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;
    protected final int sensorType;

    protected SensorMiniGameModel(SensorResponseModel expectedResponseModel) {
        super(expectedResponseModel);
        sensorType = expectedResponse.sensorType;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value = sensorEvent.values[0];
        if(handleResponse(new SensorResponseModel(sensorType, value))) {
            pauseSensor();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void resumeSensor(Context context) {
        sensorManager = platformFeaturesProvider.getSensorManager(context);
        sensor = sensorManager.getDefaultSensor(sensorType);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pauseSensor() {
        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
