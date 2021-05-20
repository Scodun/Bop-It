package com.se2.bopit.domain;

import android.hardware.Sensor;

public class CoverLightSensorMiniGameModel extends SensorMiniGameModel {

    public static final int SENSOR_TYPE = Sensor.TYPE_LIGHT;
    public static final float THRESHOLD = 10;

    public CoverLightSensorMiniGameModel() {
        super(new SensorResponseModel(SENSOR_TYPE, THRESHOLD));
    }

    @Override
    protected boolean checkResponse(SensorResponseModel response) {
        return response.values[0] < expectedResponse.values[0];
    }
}
