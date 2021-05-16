package com.se2.bopit.domain;

import android.hardware.Sensor;

public class CoverLightSensorMiniGameModel extends SensorMiniGameModel {

    public CoverLightSensorMiniGameModel() {
        super(new SensorResponseModel(Sensor.TYPE_LIGHT, 10));
    }

    @Override
    protected boolean checkResponse(SensorResponseModel response) {
        return response.values[0] < expectedResponse.values[0];
    }
}
