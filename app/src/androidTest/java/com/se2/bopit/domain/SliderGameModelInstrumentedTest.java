package com.se2.bopit.domain;

import android.content.Context;
import android.widget.SeekBar;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.se2.bopit.domain.gamemodel.SliderGameModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class SliderGameModelInstrumentedTest {

    Context appContext;
    SliderGameModel model;

    @Before
    public void setup() {
        model = new SliderGameModel();
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testSetupSliderProgress() {
        SeekBar slider = new SeekBar(appContext);

        SliderGameModel.setupSlider(slider);

        assertNotEquals(0, slider.getProgress());
    }

}
