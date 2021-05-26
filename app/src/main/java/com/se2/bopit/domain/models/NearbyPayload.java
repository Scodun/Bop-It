package com.se2.bopit.domain.models;

import java.io.Serializable;

/**
 * Types:
 * 0... User update (Lobby)
 *
 * Payload: json string
 */
public class NearbyPayload implements Serializable{
    private String payload;
    private int type;

    public NearbyPayload(int type, String payload){
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
}
