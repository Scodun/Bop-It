package com.se2.bopit.domain.interfaces;

import android.os.CountDownTimer;

import java.util.function.LongConsumer;

public interface PlatformFeaturesProvider {

    CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval,
                                        LongConsumer onTickHandler,
                                        Runnable onFinishHandler);
}
