package com.se2.bopit.domain;

import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameRulesTest {

    static final Class<?>[] TEST_GAMES = {
            ColorButtonMiniGame.class,
            CoverLightSensorMiniGame.class,
            WeirdTextButtonMiniGame.class};

    GameRules gameRules;

    @Before
    public void setUp() throws Exception {
        gameRules = new GameRules(TEST_GAMES);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void resetToDefault() {
        gameRules.setAvoidRepeatingGameTypes(true);
        gameRules.resetToDefault();

        assertFalse(gameRules.isAvoidRepeatingGameTypes());
    }

    @Test
    public void disablePermanently() {
        gameRules.getItems().forEach(i -> assertTrue(i.isAvailable()));
        gameRules.disablePermanently(CoverLightSensorMiniGame.class);
        gameRules.getItems().forEach(i -> {
            if (i.type == CoverLightSensorMiniGame.class) {
                assertFalse(i.isAvailable());
                assertFalse(i.isEnabled());
            } else {
                assertTrue(i.isAvailable());
            }
        });
    }

    @Test
    public void getItems() {
        assertEquals(3, gameRules.getItems().size());
    }

    @Test
    public void getEnabledItems() {
        assertEquals(2, gameRules.getEnabledItems().size());
    }
}