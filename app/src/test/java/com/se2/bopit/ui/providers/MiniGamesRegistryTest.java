package com.se2.bopit.ui.providers;

import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MiniGamesRegistryTest {

    MiniGamesRegistry registry;

    @Before
    public void setUp() {
        registry = MiniGamesRegistry.getInstance();
    }

    @Test
    public void createRandomMiniGameWithDefaultRules() {
        int typesCount = MiniGamesRegistry.GAME_TYPES.length;
        int countDisabledByDefault = 1; // see @MiniGameType

        Set<Class<?>> createdTypes = new HashSet<>();
        for(int i = 0; i < typesCount * 10; i++) {
            MiniGame game = registry.createRandomMiniGame();
            assertNotNull(game);
            createdTypes.add(game.getClass());
            assertTrue(Arrays.stream(MiniGamesRegistry.GAME_TYPES)
                    .anyMatch(c -> c == game.getClass()));
        }

        assertEquals(typesCount - countDisabledByDefault, createdTypes.size());
    }
}