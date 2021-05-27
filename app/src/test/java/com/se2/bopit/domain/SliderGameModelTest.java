package com.se2.bopit.domain;

import android.widget.SeekBar;
import com.se2.bopit.domain.mock.GameListenerMock;
import com.se2.bopit.domain.sliderminigame.SliderGameModel;
import com.se2.bopit.domain.sliderminigame.SliderResponseModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class SliderGameModelTest {

    SliderGameModel model;
    SeekBar slider;
    SliderResponseModel response;
    GameListenerMock listener;

    //@BeforeEach
    public void setup() {
        model = new SliderGameModel();
        slider = new SeekBar(null);
        response = new SliderResponseModel(slider, 0);
        listener = new GameListenerMock();
    }

    @Test
    public void testCheckResponseCorrect() {
        setup();

        response.progress = model.target;

        assertTrue(model.checkResponse(response));
    }

    @Test
    public void testCheckResponseIncorrect() {
        setup();

        response.progress = model.target + 1;

        assertFalse(model.checkResponse(response));
    }

    @Test
    public void testHandleResponseCorrect() {
        setup();

        response.progress = model.target;

        model.setGameListener(listener);

        assertTrue(model.handleResponse(response));
    }

    @Test
    public void testHandleResponseIncorrect() {
        setup();

        response.progress = model.target + 1;

        model.setGameListener(listener);

        assertFalse(model.handleResponse(response));
    }

    @Test
    public void testHandleResponseListenerValueCorrect() {
        setup();

        response.progress = model.target;

        model.setGameListener(listener);

        model.handleResponse(response);

        assertTrue(listener.result);
    }

    @Test
    public void testHandleResponseListenerValueIncorrect() {
        setup();

        response.progress = model.target + 1;

        model.setGameListener(listener);

        model.handleResponse(response);

        assertFalse(listener.result);
    }

    @Test
    public void testGenerateProgress() {
        // Test if the random number are as expected 1000 times
        boolean success = true;

        for (int i=0; i<1000; i++) {
            int progress = SliderGameModel.generateProgress();
            if ((1 <= progress && progress <= 6) || (10 <= progress && progress <= 14)) {
                success = false;
                break;
            }
        }
        assertTrue(success);
    }

    @Test
    public void testGenerateTarget() {
        // Test if the random number are as expected 1000 times
        boolean success = true;

        for (int i=0; i<1000; i++) {
            int progress = SliderGameModel.generateTarget();
            if (7 <= progress && progress <= 9) {
                success = false;
                break;
            }
        }
        assertTrue(success);
    }
}