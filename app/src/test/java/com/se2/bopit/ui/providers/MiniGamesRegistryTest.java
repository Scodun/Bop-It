package com.se2.bopit.ui.providers;

import com.se2.bopit.domain.GameRuleItemModel;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.exception.GameCreationException;
import com.se2.bopit.ui.ButtonMiniGameFragment;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MiniGamesRegistryTest {

    MiniGamesRegistry registry;
    Context contextMock;
    SensorManager sensorManagerMock;

    @Before
    public void setUp() {
        registry = MiniGamesRegistry.getInstance();
        contextMock = mock(Context.class);
        sensorManagerMock = mock(SensorManager.class);
        doReturn(sensorManagerMock).when(contextMock).getSystemService(eq(Context.SENSOR_SERVICE));
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

    @Test
    public void createMiniGameException() {
        GameRuleItemModel model = new GameRuleItemModel(ButtonMiniGameFragment.class);
        assertThrows(GameCreationException.class, () -> registry.createMiniGame(model));
    }

    @Test
    public void checkAvailability() {
        doReturn(Collections.emptyList()).when(sensorManagerMock).getSensorList(anyInt());
        registry.checkAvailability(contextMock);

        assertFalse(registry.availableSensorTypes.get(Sensor.TYPE_LIGHT));
        assertTrue(registry.gameRules.getItems().stream()
                .anyMatch(i -> i.type == CoverLightSensorMiniGame.class && !i.available));
    }
}