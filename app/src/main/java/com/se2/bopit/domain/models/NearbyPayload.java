package com.se2.bopit.domain.models;

import java.io.Serializable;

public class NearbyPayload implements Serializable{
    public String payload;
    public int type;

    public NearbyPayload(int type, String payload){
        this.type = type;
        this.payload = payload;
    }
}
