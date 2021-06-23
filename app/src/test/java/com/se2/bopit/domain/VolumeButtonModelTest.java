package com.se2.bopit.domain;

import com.se2.bopit.domain.responsemodel.VolumeButtonModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VolumeButtonModelTest {

    VolumeButtonModel volumeButtonModel;
    VolumeButton volumeButton;

    @Before
    public void setUp() throws Exception {
        volumeButtonModel = new VolumeButtonModel(VolumeButton.DOWN);
        volumeButton = VolumeButton.UP;
    }

    @After
    public void tearDown() throws Exception {
        volumeButtonModel = null;
    }

    @Test
    public void labelTest() {
        assertEquals("DOWN", volumeButtonModel.getLabel());
    }

    @Test
    public void volumeButtonTest() {
        volumeButtonModel.setVolumeButton(volumeButton);
        assertEquals("UP", volumeButtonModel.getLabel());
    }
}