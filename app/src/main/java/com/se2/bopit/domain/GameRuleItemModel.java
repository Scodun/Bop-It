package com.se2.bopit.domain;

import com.se2.bopit.domain.annotations.MiniGameType;

public class GameRuleItemModel {
    static final String[] TYPE_NAME_IGNORE_SUFFIX = {"minigame", "fragment", "activity"};
    static final String[] TYPE_NAME_IGNORE_PREFIX = {"minigame"};

    public final Class<?> type;
    public final String name;
    private boolean enabled;

    private boolean available = true; // by default all are available

    public GameRuleItemModel(Class<?> type) {
        this.type = type;
        this.name = extractTypeName(type);
        this.setEnabled(isEnabledByDefault());
    }

    public void reset() {
        this.setEnabled(isAvailable() && isEnabledByDefault());
    }

    public void disablePermanently() {
        setAvailable(false);
        setEnabled(false);
    }

    boolean isEnabledByDefault() {
        if (!isAvailable())
            return false;
        MiniGameType miniGameType = type.getAnnotation(MiniGameType.class);
        return miniGameType != null ? miniGameType.enableByDefault() : MiniGameType.DEFAULT_ENABLED;
    }

    static String extractTypeName(Class<?> type) {
        MiniGameType miniGameType = type.getAnnotation(MiniGameType.class);
        String name = null;
        if (miniGameType != null && !MiniGameType.DEFAULT_NAME.equals(miniGameType.name())) {
            name = miniGameType.name();
        }
        // process default name
        if (name == null) {
            name = type.getSimpleName();
            name = removeSuffixes(name);
            name = removePrefixes(name);
            name = splitCamelCase(name);
        }
        return name;
    }

    static String removeSuffixes(String src) {
        String name = src;
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
        return name;
    }

    static String removePrefixes(String src) {
        String name = src;
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
        return name;
    }

    static String splitCamelCase(String src) {
        return src.replaceAll("([a-z])([A-Z][a-z])", "$1 $2");
    }

    @Override
    public String toString() {
        return "GameRuleItemModel{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", enabled=" + isEnabled() +
                '}';
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * if {@code false} then the game is not safe to use at all (e.g. missing hardware)
     * and thus, must be treated as unavailable.
     */
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
