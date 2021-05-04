package com.se2.bopit.platform;

import android.os.CountDownTimer;

import com.se2.bopit.domain.providers.PlatformFeaturesProvider;

import java.util.function.LongConsumer;

public class AndroidPlatformFeaturesProvider implements PlatformFeaturesProvider {
    @Override
    public CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval,
                                               LongConsumer onTickHandler,
                                               Runnable onFinishHandler) {
        return new CountDownTimer(millisInFuture, countDownInterval) {
            //@RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long l) {
                if(onTickHandler != null) {
                    onTickHandler.accept(l);
                }
            }

            @Override
            public void onFinish() {
                if(onFinishHandler != null) {
                    onFinishHandler.run();
                }
            }
        };
    }
}
