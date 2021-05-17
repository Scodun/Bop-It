package com.se2.bopit.ui.games;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.se2.bopit.domain.interfaces.GameListener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class PlacePhoneMiniGameTest {

    private static final String TAG = "PlacePhoneMiniGameTEST";

    PlacePhoneMiniGame game;

    @Before
    public void setUp() {
        game = new PlacePhoneMiniGame();
    }

    @Test
    public void phoneNotFlatTEST() {

        float[] currentPhonePosition = {10,10,10};
        try{
            GameListener listener = r -> {};
            game.setGameListener(listener);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertFalse(game.getIsFlat());

        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }
    @Test
    public void phoneFlatTEST() {

        float[] currentPhonePosition = {0,0,100};
        try{
            GameListener listener = r -> {};
            game.setGameListener(listener);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertTrue(game.getIsFlat());

        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    @Test
    public void sensorExceptionTEST() {

        float[] currentPhonePosition = {0,0,100};
        try{
            GameListener listener = r -> {};
            game.setGameListener(null);

            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
        }
        catch (Exception ex){
            fail("Unexpected exception: " + ex);
        }
    }
    @After
    public void teardown(){
        game = null;
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