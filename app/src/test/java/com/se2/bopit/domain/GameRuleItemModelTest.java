package com.se2.bopit.domain;

import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameRuleItemModelTest {

    @Test
    public void reset() {
        GameRuleItemModel item = new GameRuleItemModel(SimpleTextButtonMiniGame.class);
        assertTrue(item.enabled);

        item.enabled = false;
        assertFalse(item.enabled);

        item.reset();
        assertTrue(item.enabled);
    }

    @Test
    public void isEnabledByDefault() {
        assertTrue(new GameRuleItemModel(ColorButtonMiniGame.class).isEnabledByDefault());
    }

    @Test
    public void isEnabledByDefaultFalseByAnnotation() {
        assertFalse(new GameRuleItemModel(WeirdTextButtonMiniGame.class).isEnabledByDefault());
    }

    @Test
    public void extractTypeNameWithAnnotation() {
        assertEquals("Color Buttons", GameRuleItemModel.extractTypeName(ColorButtonMiniGame.class));
    }

    @Test
    public void extractTypeNameWithoutAnnotation() {
        assertEquals("Image Button", GameRuleItemModel.extractTypeName(ImageButtonMinigame.class));
    }

}