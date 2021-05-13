package com.se2.bopit.domain;

import com.se2.bopit.domain.annotations.MiniGameType;

public class GameRuleItemModel {
    static final String[] TYPE_NAME_IGNORE_SUFFIX = {"minigame", "fragment", "activity"};
    static final String[] TYPE_NAME_IGNORE_PREFIX = {"minigame"};

    public final Class<?> type;
    public final String name;
    public boolean enabled;

    public GameRuleItemModel(Class<?> type) {
        this.type = type;
        this.name = extractTypeName(type);
        this.enabled = isEnabledByDefault();
    }

    public void reset() {
        this.enabled = isEnabledByDefault();
    }

    boolean isEnabledByDefault() {
        MiniGameType miniGameType = type.getAnnotation(MiniGameType.class);
        return miniGameType != null ? miniGameType.enableByDefault() : MiniGameType.DEFAULT_ENABLED;
    }

    static String extractTypeName(Class<?> type) {
        MiniGameType miniGameType = type.getAnnotation(MiniGameType.class);
        String name = null;
        if(miniGameType != null && !MiniGameType.DEFAULT_NAME.equals(miniGameType.name())) {
            name = miniGameType.name();
        }
        // process default name
        if(name == null) {
            name = type.getSimpleName();
            for (String stopword : TYPE_NAME_IGNORE_SUFFIX) {
                int nameLength = name.length();
                int stopwordLength = stopword.length();
                if (nameLength <= stopwordLength) {
                    continue;
                }
                if (name.toLowerCase().endsWith(stopword)) {
                    name = name.substring(0, nameLength - stopwordLength);
                }
            }
            for (String stopword : TYPE_NAME_IGNORE_PREFIX) {
                int nameLength = name.length();
                int stopwordLength = stopword.length();
                if (nameLength <= stopwordLength) {
                    continue;
                }
                if (name.toLowerCase().startsWith(stopword)) {
                    name = name.substring(stopwordLength);
                }
            }
            name = name.replaceAll("([a-z])([A-Z])", "$1 $2");
        }
        return name;
    }
}
