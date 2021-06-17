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

    /**
     * sets up test class
     */
    @Before
    public void setUp() {
        game = new PlacePhoneMiniGame();
    }

    /**
     * Tests methods isflat and hasmoved
     * Combination: Not flat, not moved
     */
    @Test
    public void phoneNotFlatNotMovedTEST() {

        float[] currentPhonePosition = {10, 10, 10};
        try {
            GameListener listener = r -> {
            };
            game.setGameListener(listener);
            game.setCurrent(17f);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertFalse(game.getIsFlat());
            Assert.assertFalse(game.getHasMoved());

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Tests methods isflat and hasmoved
     * Combination: Not flat, moved
     */
    @Test
    public void phoneNotFlatMovedTEST() {

        float[] currentPhonePosition = {10, 10, 10};
        try {
            GameListener listener = r -> {
            };
            game.setGameListener(listener);
            game.setCurrent(5.1f);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertFalse(game.getIsFlat());
            Assert.assertTrue(game.getHasMoved());

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Tests methods isflat and hasmoved
     * Combination: flat, not moved
     */
    @Test
    public void phoneFlatNotMovedTEST() {

        float[] currentPhonePosition = {0, 0, 100};
        try {
            GameListener listener = r -> {
            };
            game.setGameListener(listener);
            game.setCurrent(99f);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertTrue(game.getIsFlat());
            Assert.assertFalse(game.getHasMoved());


        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Tests methods isflat and hasmoved
     * Combination: flat, moved
     */
    @Test
    public void phoneFlatAndMovedTEST() {

        float[] currentPhonePosition = {0, 0, 100};
        try {
            GameListener listener = r -> {
            };
            game.setGameListener(listener);
            game.setCurrent(15f);
            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
            Assert.assertTrue(game.getIsFlat());
            Assert.assertTrue(game.getHasMoved());

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Checks exception in onsensor changed
     */
    @Test
    public void sensorExceptionTEST() {

        float[] currentPhonePosition = {0, 0, 100};
        try {
            GameListener listener = r -> {
            };
            game.setGameListener(null);

            SensorEvent event = getSensorEvent(currentPhonePosition);
            game.onSensorChanged(event);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex);
        }
    }

    /**
     * frees minigame
     */
    @After
    public void teardown() {
        game = null;
    }

    /**
     * Mocks a sensor event, of type accelerometer
     * https://stackoverflow.com/questions/34530865/how-to-mock-motionevent-and-sensorevent-for-unit-testing-in-android
     */
    private SensorEvent getSensorEvent(float[] values) throws Exception {
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