package com.se2.bopit.domain.gamemodel;

import android.content.Context;

import com.se2.bopit.domain.SensorEventModel;
import com.se2.bopit.domain.interfaces.SensorEventModelListener;
import com.se2.bopit.domain.responsemodel.SensorResponseModel;

public abstract class SensorMiniGameModel extends ActionGameModel<SensorResponseModel>
        implements SensorEventModelListener {
    public final int sensorType;

    protected Context context;

    protected SensorMiniGameModel(SensorResponseModel expectedResponseModel) {
        super(expectedResponseModel);
        sensorType = expectedResponse.sensorType;
    }

    @Override
    public void onSensorChanged(SensorEventModel sensorEvent) {
        float value = sensorEvent.values[0];
        if (handleResponse(new SensorResponseModel(sensorType, value))) {
            pauseSensor();
        }
    }

    public void resumeSensor(Context context) {
        this.context = context;
        platformFeaturesProvider.registerSensorListener(context, sensorType, this);
    }

    public void pauseSensor() {
        if (context != null) {
            platformFeaturesProvider.unregisterSensorListener(context, this);
        }
    }
}
