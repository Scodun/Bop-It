package com.se2.bopit.ui.providers;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.GameRuleItemModel;
import com.se2.bopit.domain.GameRules;
import com.se2.bopit.domain.annotations.RequireSensor;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.interfaces.MiniGamesProvider;
import com.se2.bopit.exception.GameCreationException;
import com.se2.bopit.ui.games.ColorButtonMiniGame;
import com.se2.bopit.ui.games.CoverLightSensorMiniGame;
import com.se2.bopit.ui.games.DrawingMinigame;
import com.se2.bopit.ui.games.ImageButtonMinigame;
import com.se2.bopit.ui.games.PlacePhoneMiniGame;
import com.se2.bopit.ui.games.RightButtonCombination;
import com.se2.bopit.ui.games.ShakePhoneMinigame;
import com.se2.bopit.ui.games.SimpleTextButtonMiniGame;
import com.se2.bopit.ui.games.SpeechRecognitionMiniGame;
import com.se2.bopit.ui.games.VolumeButtonMinigame;
import com.se2.bopit.ui.games.WeirdTextButtonMiniGame;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MiniGamesRegistry implements MiniGamesProvider {
    static final String TAG = MiniGamesRegistry.class.getSimpleName();

    static final Random RND = new Random();

    static final Class<?>[] GAME_TYPES = {
            // add new class here, no matter in which order
            ColorButtonMiniGame.class,
            SimpleTextButtonMiniGame.class,
            WeirdTextButtonMiniGame.class,
            ImageButtonMinigame.class,
            RightButtonCombination.class,
            ShakePhoneMinigame.class,
            PlacePhoneMiniGame.class,
            CoverLightSensorMiniGame.class,
            VolumeButtonMinigame.class,
            DrawingMinigame.class,
            SpeechRecognitionMiniGame.class
    };

    final Map<Integer, Boolean> availableSensorTypes = new HashMap<>();

    static MiniGamesRegistry instance;

    public final GameRules gameRules;

    protected MiniGamesRegistry(GameRules gameRules) {
        this.gameRules = gameRules;
        Log.d(TAG, "MiniGamesRegistry created");
    }

    public static MiniGamesRegistry getInstance() {
        if (instance == null) {
            instance = new MiniGamesRegistry(
                    new GameRules(GAME_TYPES));
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void checkAvailability(Context context) {
        if (!availableSensorTypes.isEmpty())
            return; // already checked

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        for (Class<?> type : GAME_TYPES) {
            RequireSensor requireSensor = type.getAnnotation(RequireSensor.class);
            if (requireSensor != null) {
                int sensorType = requireSensor.value();
                boolean available = availableSensorTypes.computeIfAbsent(sensorType,
                        s -> !sensorManager.getSensorList(s).isEmpty());
                if (!available) {
                    gameRules.disablePermanently(type);
                    Log.d(TAG, "Sensor " + sensorType
                            + " is not available => disabling game type " + type.getSimpleName());
                }
            }
        }
    }

    GameRuleItemModel lastGameType;

    @Override
    public MiniGame createRandomMiniGame() {
        List<GameRuleItemModel> items = gameRules.getEnabledItems();
        int itemsSize = items.size();
        boolean avoidRepeating = gameRules.avoidRepeatingGameTypes && itemsSize > 2;

        if (itemsSize == 0) {
            // ignore invalid settings
            Log.w(TAG, "No games enabled! => ignoring preferences.");
            items = gameRules.getItems();
        }

        GameRuleItemModel item;
        do {
            item = items.get(RND.nextInt(itemsSize));
        } while (avoidRepeating && item == lastGameType);

        lastGameType = item;
        return createMiniGame(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public MiniGame createMiniGame(GameRoundModel round) {
        try {
            Class<?> cls = Class.forName("com.se2.bopit.ui.games." + round.getGameType());
            Object model;
            if (round.getModelType() != null && round.getModelJson() != null) {
                Class<?> modelType = Class.forName("com.se2.bopit.domain.gamemodel." + round.getModelType());
                Gson gson = new Gson();
                model = gson.fromJson(round.getModelJson(), modelType);
            } else {
                model = null;
            }

            Constructor<?> constructor = null;
            if (model != null) {
                constructor = Arrays.stream(cls.getConstructors())
                        .filter(c -> c.getParameterTypes().length == 1
                                && c.getParameterTypes()[0].isInstance(model))
                        .findFirst().orElse(null);
            }
            if (constructor != null) {
                return (MiniGame) constructor.newInstance(model);
            }

            // fallback with drawback - games may be not the same!!!
            return (MiniGame) cls
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception ex) {
            String msg = "Error creating minigame from model: " + round.getGameType();
            Log.e(TAG, msg, ex);
            throw new GameCreationException(msg, ex);
        }
    }

    MiniGame createMiniGame(GameRuleItemModel model) {
        try {
            return (MiniGame) model.type
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            String msg = "Error creating minigame from model: " + model;
            Log.e(TAG, msg, e);
            throw new GameCreationException(msg, e);
        }
    }
}
