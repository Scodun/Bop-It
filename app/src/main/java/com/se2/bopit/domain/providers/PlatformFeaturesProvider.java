package com.se2.bopit.domain.providers;

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
}
