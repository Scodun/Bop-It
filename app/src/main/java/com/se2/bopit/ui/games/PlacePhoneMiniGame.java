package com.se2.bopit.ui.games;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private GameListener listener;
    private static final String TAG = "PlacePhoneMiniGame";
    private boolean isFlat = false;
    private boolean hasMoved = false;
    private float current;
    private float last;
    private float accel;



    public PlacePhoneMiniGame() {
        super(R.layout.fragment_button_component);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);
        ImageView imageView = view.findViewById(R.id.actionImage);
        imageView.setImageResource(R.drawable.phone_flat);
        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText("Place your phone on a flat surface");
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        current = SensorManager.GRAVITY_EARTH;
        last = SensorManager. GRAVITY_EARTH;

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
            if(isFlat(event.values.clone()) && hasMoved(event.values.clone())){
                if(sensorManager != null){
                    sensorManager.unregisterListener(this);
                }
                listener.onGameResult(true);
                hasMoved = true;
                isFlat = true;

            }
        }
        catch (Exception ex){
            Log.e(TAG,ex.getMessage());
        }
    }

    /**
     * Needed for implementing SensorEventListener
     * This specific method is not used.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // needed for SensorEventListener. Is empty on purpose
    }
    /**
     * @param vals - values received from sensor
     * Checks if phone is placed flat
     * There is a realtive generous error margin calculated in, to account for Camera bumps.
     */
    private boolean isFlat(float[] vals){
        //Source for calculation: https://stackoverflow.com/questions/11175599/how-to-measure-the-tilt-of-the-phone-in-xy-plane-using-accelerometer-in-android/15149421#15149421
        double gNorm = Math.sqrt(vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2]);

        // Normalize the accelerometer vector
        vals[0] = (float) (vals[0] / gNorm);
        vals[1] = (float) (vals[1] / gNorm);
        vals[2] = (float) (vals[2] / gNorm);

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(vals[2])));

        return inclination < 15 || inclination > 155;
    }
    /**
     * @param vals - values received from sensor
     * Checks if phone was moved
     * This method was added to prevent getting double points if this minigame gets selected twice or more often in a row. So the phone has to be moved first.
     */
    private boolean hasMoved(float[] vals){
        //Source for calculation: https://stackoverflow.com/questions/11175599/how-to-measure-the-tilt-of-the-phone-in-xy-plane-using-accelerometer-in-android/15149421#15149421
        last = current;
        current = (float) Math.sqrt(vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2]);
        float sub = current - last;
        accel = accel * 0.9f + sub;
        return accel > 2;
    }
    public boolean getIsFlat(){
        return isFlat;
    }
    public boolean getHasMoved(){
        return hasMoved;
    }
}