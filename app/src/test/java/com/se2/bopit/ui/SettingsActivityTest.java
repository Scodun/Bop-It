package com.se2.bopit.ui;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SettingsActivityTest {
    SettingsActivity settingsActivity;


    @Before
    public void setUp() {
        settingsActivity = new SettingsActivity();
    }

    @Test
    public void testSavePrefValues() {
    }

    @After
    public void tearDown() {
        settingsActivity = null;
    }
}
