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
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;


/**
 * Minigame
 *
 * Goal: Place Phone on a flat surface.
 * Calls the MainActivity onGameStart Listener to display the Fragment
 * Sets the GameListener for the Minigame
 */
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
    /**
     * gets called as soon as the accelerator sensor changes
     * This method calls isFlat, and if isFlat returns true, we call the GameListener
     * and tell it that the Minigame was completed successfully
     * unregisters listener on game completion
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        try{
            if(isFlat(event.values.clone())){
                listener.onGameResult(true);
                sensorManager.unregisterListener(this);
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }


    }

    /**
     * Needed for implementing SensorEventListener
     * This specific method is not used.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    /**
     * @param vals - values received from sensor
     * Checks if phone is placed flat
     * There is a realtive generous error margin calculated in, to account for Camera bumps.
     */
    private boolean isFlat(float[] vals){
        //Source for calculation: https://stackoverflow.com/questions/11175599/how-to-measure-the-tilt-of-the-phone-in-xy-plane-using-accelerometer-in-android/15149421#15149421
        double norm_Of_g = Math.sqrt(vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2]);

        // Normalize the accelerometer vector
        vals[0] = (float) (vals[0] / norm_Of_g);
        vals[1] = (float) (vals[1] / norm_Of_g);
        vals[2] = (float) (vals[2] / norm_Of_g);

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(vals[2])));

        if (inclination < 15 || inclination > 155)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}