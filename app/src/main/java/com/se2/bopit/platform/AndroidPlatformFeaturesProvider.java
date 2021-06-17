package com.se2.bopit.platform;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;

import com.se2.bopit.domain.interfaces.SensorEventModelListener;
import com.se2.bopit.domain.interfaces.PlatformFeaturesProvider;

import java.util.function.LongConsumer;

public class AndroidPlatformFeaturesProvider implements PlatformFeaturesProvider {

    @Override
    public CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval,
                                               LongConsumer onTickHandler,
                                               Runnable onFinishHandler) {
        return new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                if (onTickHandler != null) {
                    onTickHandler.accept(l);
                }
            }

            @Override
            public void onFinish() {
                if (onFinishHandler != null) {
                    onFinishHandler.run();
                }
            }
        };
    }

    @Override
    public void registerSensorListener(Context context, int sensorType,
                                       SensorEventModelListener listener) {
        SensorManager mgr = getSensorManager(context);
        Sensor sensor = mgr.getDefaultSensor(sensorType);
        SensorEventListener sensorListener = SensorEventListenerWrapper.wrap(listener);
        mgr.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void unregisterSensorListener(Context context, SensorEventModelListener listener) {
        SensorEventListener sensorListener = SensorEventListenerWrapper.wrap(listener);
        getSensorManager(context).unregisterListener(sensorListener);
    }
}
