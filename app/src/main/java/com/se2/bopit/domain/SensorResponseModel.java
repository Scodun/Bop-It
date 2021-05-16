package com.se2.bopit.domain;

public class SensorResponseModel extends ResponseModel {
    public final int sensorType;
    public final float[] values;

    public SensorResponseModel(int sensorType, float... values) {
        this.sensorType = sensorType;
        this.values = values;
    }
}
