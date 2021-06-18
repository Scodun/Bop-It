package com.se2.bopit.domain.gamemodel;

import android.hardware.Sensor;

import com.se2.bopit.domain.responsemodel.SensorResponseModel;

public class CoverLightSensorMiniGameModel extends SensorMiniGameModel {

    public static final int SENSOR_TYPE = Sensor.TYPE_LIGHT;
    public static final float THRESHOLD = 10;

    public CoverLightSensorMiniGameModel() {
        super(new SensorResponseModel(SENSOR_TYPE, THRESHOLD));
    }

    @Override
    public boolean checkResponse(SensorResponseModel response) {
        return response.values[0] < expectedResponse.values[0];
    }

}
