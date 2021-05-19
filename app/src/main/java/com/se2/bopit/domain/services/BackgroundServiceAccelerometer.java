package com.se2.bopit.domain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.se2.bopit.IntentFactory;


public class BackgroundServiceAccelerometer extends Service implements SensorEventListener {
    private static final float GRAVITY_EARTH = SensorManager.GRAVITY_EARTH;
    private SensorManager sensorManager;
    private LocalBroadcastManager broadcastManager;

    public static final String SHAKE_ACTION = "com.se2.bopit.ui.games.SHAKE";
    public static final String SHAKED = "isShaking";
    public boolean isShaked;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        isShaked = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_GAME);

        return START_STICKY;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        IntentFactory intentFactory = new IntentFactory(this);
        Intent intent;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float mAccel = (x * x + y * y + z * z) / (GRAVITY_EARTH * GRAVITY_EARTH);

            if (mAccel > 2) {
                isShaked = true;
                intent = intentFactory.accelIntent(isShaked);
                sensorManager = null;
            } else {
                isShaked = false;
                intent = intentFactory.accelIntent(isShaked);
            }
            broadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //no need
    }

    public boolean isShaked(){
        return isShaked;
    }
}
