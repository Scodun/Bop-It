package com.se2.bopit.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RightButtonModelTest {

    RightButtonModel rightButtonModel;

    @Before
    public void setUp() throws Exception {
        rightButtonModel = new RightButtonModel(RightButton.RIGHT);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void labelTest(){
        assertEquals("RIGHT",rightButtonModel.label);
    }
}