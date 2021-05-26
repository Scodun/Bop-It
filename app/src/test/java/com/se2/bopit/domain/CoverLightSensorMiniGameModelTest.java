package com.se2.bopit.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoverLightSensorMiniGameModelTest {

    CoverLightSensorMiniGameModel model;

    @Before
    public void setUp() throws Exception {
        model = new CoverLightSensorMiniGameModel();
    }

    @Test
    public void checkResponseAboveThreshold() {
        assertFalse(model.checkResponse(new SensorResponseModel(
                CoverLightSensorMiniGameModel.SENSOR_TYPE,
                CoverLightSensorMiniGameModel.THRESHOLD)));
    }

    @Test
    public void checkResponseBelowThreshold() {
        assertTrue(model.checkResponse(new SensorResponseModel(
                CoverLightSensorMiniGameModel.SENSOR_TYPE,
                CoverLightSensorMiniGameModel.THRESHOLD - 0.1f)));
    }
}