package com.se2.bopit.domain.data.websocket;

import com.se2.bopit.domain.models.NearbyPayload;

public class IncomingMessage {
    private String from;
    private NearbyPayload data;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public NearbyPayload getData() {
        return data;
    }

    public void setData(NearbyPayload data) {
        this.data = data;
    }
}
