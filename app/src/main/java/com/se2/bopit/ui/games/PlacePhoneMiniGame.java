package com.se2.bopit.ui.games;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.SensorModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.DrawLineCanvas;
import com.se2.bopit.ui.SensorMiniGameFragment;

import java.util.ArrayList;
import java.util.Collections;


public class PlacePhoneMiniGame extends Fragment implements SensorEventListener, MiniGame {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static GameListener listener;

    public PlacePhoneMiniGame() {
        super(R.layout.fragment_button_component);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_component, container, false);
        LinearLayout layout = view.findViewById(R.id.buttonsRegion);
        TextView messageText = view.findViewById(R.id.messageText);
        messageText.setText("Place your phone on a flat surface");
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);


        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        try{
            float[] g = new float[3];
            g = event.values.clone();

            double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

// Normalize the accelerometer vector
            g[0] = (float) (g[0] / norm_Of_g);
            g[1] = (float) (g[1] / norm_Of_g);
            g[2] = (float) (g[2] / norm_Of_g);

            int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

            if (inclination < 15 || inclination > 155)
            {
                listener.onGameResult(true);
                sensorManager.unregisterListener(this);
            }
            else
            {
                System.out.println("notflat");
            }
        }
        catch (Exception ex){
            System.out.println("asdf");

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("asdf");
    }
}