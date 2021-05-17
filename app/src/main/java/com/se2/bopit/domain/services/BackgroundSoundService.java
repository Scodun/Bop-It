package com.se2.bopit.domain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.se2.bopit.R;

public class BackgroundSoundService extends Service {
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_SOUND = "sound";
    private MediaPlayer mediaplayer;
    private SharedPreferences customSharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        mediaplayer = MediaPlayer.create(this, R.raw.riseandshine);
        mediaplayer.setLooping(true);
        mediaplayer.setVolume(0.5f, 0.5f);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean backgroundMusic = customSharedPreferences.getBoolean(PREF_KEY_SOUND, false);
        if (backgroundMusic) {
            mediaplayer.start();
        } else {
            mediaplayer.stop();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaplayer != null) {
            mediaplayer.stop();
            mediaplayer.release();
        }
    }
}