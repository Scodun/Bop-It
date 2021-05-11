package com.se2.bopit.domain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.se2.bopit.R;


public class BackgroundServiceAccelerometer extends Service implements SensorEventListener {
    private static final float GRAVITY_EARTH = SensorManager.GRAVITY_EARTH;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private float mAccel;
    private Intent intent;
    private final Handler handler = new Handler();
    private LocalBroadcastManager broadcastManager;

    public static final String SHAKE_ACTION = "com.se2.bopit.ui.games.SHAKE";
    public static final String SHAKED = "isShaking";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            mAccel = (x * x + y * y + z * z) / (GRAVITY_EARTH * GRAVITY_EARTH);

            Intent intent = new Intent(SHAKE_ACTION);
            if (mAccel > 2) {
                intent.putExtra(SHAKED, true);
            } else {
                intent.putExtra(SHAKED, false);
            }
            broadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
