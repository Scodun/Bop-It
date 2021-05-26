package com.se2.bopit.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RightButtonModelTest {

    RightButtonModel rightButtonModel;

    @Before
    public void setUp() throws Exception {
        rightButtonModel = new RightButtonModel(RightButton.RIGHT);
    }

    @After
    public void tearDown() throws Exception {
        rightButtonModel = null;
    }

    @Test
    public void labelTest() {
        assertEquals("RIGHT", rightButtonModel.label);
    }
}