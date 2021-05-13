package com.se2.bopit.domain;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.se2.bopit.R;

public class SoundEffects {

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int effectSound;
    private final int MAX = 2;


    public SoundEffects(Context context, int effectID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes
                    .USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setMaxStreams(MAX).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(MAX, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(effectSound, 1.0f, 1.0f, 0, 0, 1);
            }
        });
        switch (effectID) {
            case 0:
                effectSound = soundPool.load(context, R.raw.point, 1);
                break;
            case 1:
                effectSound = soundPool.load(context, R.raw.fail, 1);
                break;
        }
    }

}
