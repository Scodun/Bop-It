package com.se2.bopit.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageButtonModelTest {

    ImageButtonModel imageButtonModel;

    @Before
    public void setUp() throws Exception {
        imageButtonModel = new ImageButtonModel(ButtonImage.CAT);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void labelTest(){
        assertEquals("CAT",imageButtonModel.label);
    }
}