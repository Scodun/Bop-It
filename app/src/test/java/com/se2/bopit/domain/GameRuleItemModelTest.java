package com.se2.bopit.domain;

import com.se2.bopit.ui.MiniGameFragment;
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
    public void extractTypeNameWithoutAnnotationWithSuffix() {
        assertEquals("Image Button", GameRuleItemModel.extractTypeName(ImageButtonMinigame.class));
    }

    @Test
    public void extractTypeNameWithoutAnnotationWithPrefix() {
        assertEquals("Dummy", GameRuleItemModel.extractTypeName(MinigameDummy.class));
    }

    @Test
    public void extractTypeNameShort() {
        assertEquals("Dummy", GameRuleItemModel.extractTypeName(Dummy.class));
    }

    @Test
    public void removeSuffixes() {
        assertEquals("Test", GameRuleItemModel.removeSuffixes("TestMinigame"));
        assertEquals("Unchanged", GameRuleItemModel.removeSuffixes("Unchanged"));
    }

    @Test
    public void removePrefixes() {
        assertEquals("Test", GameRuleItemModel.removePrefixes("MinigameTest"));
        assertEquals("Unchanged", GameRuleItemModel.removePrefixes("Unchanged"));
    }

    @Test
    public void splitCamelCase() {
        assertEquals("My Test", GameRuleItemModel.splitCamelCase("MyTest"));
        assertEquals("ABC", GameRuleItemModel.splitCamelCase("ABC"));
    }

    @Test
    public void disablePermanently() {
        GameRuleItemModel item = new GameRuleItemModel(SimpleTextButtonMiniGame.class);
        assertTrue(item.available);
        assertTrue(item.isEnabledByDefault());

        item.disablePermanently();

        assertFalse(item.available);
        assertFalse(item.isEnabledByDefault());

        // even after reset
        item.reset();
        assertFalse(item.available);
        assertFalse(item.isEnabledByDefault());
    }

}

class Dummy extends MiniGameFragment {

}

class MinigameDummy extends MiniGameFragment {

}