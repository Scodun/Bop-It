package com.se2.bopit.ui;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public ActivityScenarioRule<GameActivity> activityRule =
            new ActivityScenarioRule<>(GameActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void onBackPressed() {
        assertNotNull(activityRule);
        Espresso.pressBack();
        intended(hasComponent(GamemodeSelectActivity.class.getName()));
    }
}