package com.se2.bopit.domain;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameRules {
    final Class<?>[] gameTypes;
    final Map<Class<?>, GameRuleItemModel> gameTypeModels;

    private boolean avoidRepeatingGameTypes = true;

    public GameRules(Class<?>... gameTypes) {
        this.gameTypes = gameTypes;

        IdentityHashMap<Class<?>, GameRuleItemModel> map = new IdentityHashMap<>(gameTypes.length);
        for (Class<?> type : gameTypes) {
            map.put(type, new GameRuleItemModel(type));
        }
        this.gameTypeModels = Collections.unmodifiableMap(map);
    }

    @SuppressLint("NewApi")
    public void resetToDefault() {
        setAvoidRepeatingGameTypes(false);
        gameTypeModels.forEach((k, v) -> v.reset());
    }

    public void disablePermanently(Class<?> type) {
        GameRuleItemModel model = gameTypeModels.get(type);
        if (model != null) {
            model.disablePermanently();
        } // TODO maybe else throw exception!
    }

    public List<GameRuleItemModel> getItems() {
        return new ArrayList<>(gameTypeModels.values());
    }

    @SuppressLint("NewApi")
    public List<GameRuleItemModel> getEnabledItems() {
        return gameTypeModels.values().stream()
                .filter(i -> i.enabled)
                .collect(Collectors.toList());
    }

    public boolean isAvoidRepeatingGameTypes() {
        return avoidRepeatingGameTypes;
    }

    public void setAvoidRepeatingGameTypes(boolean avoidRepeatingGameTypes) {
        this.avoidRepeatingGameTypes = avoidRepeatingGameTypes;
    }
}
