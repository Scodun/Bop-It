package com.se2.bopit.domain;

import com.se2.bopit.domain.responsemodel.ButtonModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class ButtonModelTest {
    @Test
    public void testButtonModelWithColor() {
        ButtonModel model = new ButtonModel(ButtonColor.BLUE);
        assertEquals(ButtonColor.BLUE, model.color);
        assertEquals("BLUE", model.label);
        assertFalse(model.isCorrect);
    }

    @Test
    public void testButtonModelWithLabel() {
        ButtonModel model = new ButtonModel("test");
        assertEquals("test", model.label);
        assertEquals(ButtonColor.DEFAULT, model.color);
        assertFalse(model.isCorrect);
    }

    @Test
    public void testCopyConstructor() {
        ButtonModel src = new ButtonModel(ButtonColor.RANDOM, "anything");
        src.isCorrect = true;
        ButtonModel copy = new ButtonModel(src);
        assertNotSame(copy, src);
        assertEquals(ButtonColor.RANDOM, copy.color);
        assertEquals("anything", copy.label);
        assertFalse(copy.isCorrect);
    }

}