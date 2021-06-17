package com.se2.bopit.domain.interfaces;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.CountDownTimer;

import java.util.function.LongConsumer;

public interface PlatformFeaturesProvider {

    CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval,
                                        LongConsumer onTickHandler,
                                        Runnable onFinishHandler);

    default SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    void registerSensorListener(Context context, int sensorType, SensorEventModelListener listener);

    void unregisterSensorListener(Context context, SensorEventModelListener listener);
}
