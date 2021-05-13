package com.se2.bopit.domain;

import com.se2.bopit.ui.games.ColorButtonMiniGame;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameRuleItemModelTest {

    @Test
    public void reset() {
    }

    @Test
    public void isEnabledByDefault() {
    }

    @Test
    public void extractTypeName() {
        assertEquals("Color Button", GameRuleItemModel.extractTypeName(ColorButtonMiniGame.class));
    }
}