package com.se2.bopit.ui.games;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PlacePhoneMiniGameTest {

    private static final String TAG = "PlacePhoneMiniGameTEST";
    private GameListener listener;

    PlacePhoneMiniGame game;

    @Before
    public void setUp() {
        game = new PlacePhoneMiniGame();
    }

    @Test
    public void phoneNotFlatTEST() {

        float[] currentPhonePosition = {10,10,10};
        try{
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.setGameListener(listener);

            game.onSensorChanged(event);

        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }



    }

    private SensorEvent getSensorEvent(float[] values) throws Exception{
        //https://stackoverflow.com/questions/34530865/how-to-mock-motionevent-and-sensorevent-for-unit-testing-in-android
        SensorEvent event = Mockito.mock(SensorEvent.class);
        Field sensorField = SensorEvent.class.getField("sensor");
        sensorField.setAccessible(true);
        Sensor sensor = Mockito.mock(Sensor.class);
        when(sensor.getType()).thenReturn(Sensor.TYPE_ACCELEROMETER);
        sensorField.set(event, sensor);
        Field valuesField = SensorEvent.class.getField("values");
        valuesField.setAccessible(true);
        valuesField.set(event, values);

        return event;
    }
}