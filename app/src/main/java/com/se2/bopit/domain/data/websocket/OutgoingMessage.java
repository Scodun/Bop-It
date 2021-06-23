package com.se2.bopit.domain.data.websocket;

import com.se2.bopit.domain.models.NearbyPayload;

import java.util.ArrayList;
import java.util.List;

public class OutgoingMessage {
    private List<String> to = new ArrayList<>();
    private NearbyPayload data;

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public NearbyPayload getData() {
        return data;
    }

    public void setData(NearbyPayload data) {
        this.data = data;
    }
}
