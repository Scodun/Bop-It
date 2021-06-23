package com.se2.bopit.ui.providers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.se2.bopit.domain.GameRuleItemModel;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.exception.GameCreationException;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class MiniGamesRegistryTest {

    MiniGamesRegistry registry;
    Context contextMock;
    SensorManager sensorManagerMock;

    @Before
    public void setUp() {
        registry = MiniGamesRegistry.getInstance();
        contextMock = mock(Context.class);
        sensorManagerMock = mock(SensorManager.class);
        doReturn(sensorManagerMock).when(contextMock).getSystemService(Context.SENSOR_SERVICE);
    }

    @After
    public void tearDown() {
        reset(contextMock, sensorManagerMock);
        MiniGamesRegistry.instance = null;
    }

    @Test
    public void createRandomMiniGameWithDefaultRules() {
        int typesCount = MiniGamesRegistry.GAME_TYPES.length;
        int countDisabledByDefault = 1; // see @MiniGameType

        Set<Class<?>> createdTypes = new HashSet<>();
        for (int i = 0; i < typesCount * 10; i++) {
            MiniGame game = registry.createRandomMiniGame();
            assertNotNull(game);
            createdTypes.add(game.getClass());
            assertTrue(Arrays.stream(MiniGamesRegistry.GAME_TYPES)
                    .anyMatch(c -> c == game.getClass()));
        }

        assertEquals(typesCount - countDisabledByDefault, createdTypes.size());
    }

    @Test
    public void createMiniGameException() {
        GameRuleItemModel model = new GameRuleItemModel(ButtonMiniGameFragment.class);
        assertThrows(GameCreationException.class, () -> registry.createMiniGame(model));
    }

    @Test
    @Ignore("Produces NullPointer")
    public void checkAvailability() {
        doReturn(Collections.emptyList()).when(sensorManagerMock).getSensorList(anyInt());
        registry.checkAvailability(contextMock);

        assertFalse(registry.availableSensorTypes.get(Sensor.TYPE_LIGHT));
        assertTrue(registry.gameRules.getItems().stream()
                .anyMatch(i -> i.type == CoverLightSensorMiniGame.class && !i.isAvailable()));
    }
}