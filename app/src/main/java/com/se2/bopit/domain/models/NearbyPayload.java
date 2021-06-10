package com.se2.bopit.domain.models;

import java.io.Serializable;

/**
 * Types:
 * 0... User update (Lobby)
 * <p>
 * Payload: json string
 */
public class NearbyPayload implements Serializable {
    // lobby
    public static final int USER_UPDATE = 0;

    // game rounds
    public static final int READY_TO_START = 10;
    public static final int START_NEW_ROUND = 11;
    public static final int SEND_ROUND_RESULT = 12;
    public static final int STOP_CURRENT_GAME = 13;
    public static final int NOTIFY_ROUND_RESULT = 14;
    public static final int NOTIFY_GAME_OVER = 15;
    public static final int SET_CLIENT_CHEATED = 16;
    public static final int SEND_DETECT_CHEATING = 17;
    public static final int NOTIFY_CHEAT_DETECTED = 18;

    private String payload;
    private int type;

    public NearbyPayload(int type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "type=" + type + "; payload=" + payload;
    }
}
