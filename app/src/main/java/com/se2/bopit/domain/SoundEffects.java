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
    private int shakerSound;

    public SoundEffects(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes
                    .USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        shakerSound = soundPool.load(context, R.raw.shaker, 1);
    }

    public void shakerSound(){
        soundPool.play(shakerSound,1, 1,0,0,1);
    }
}
