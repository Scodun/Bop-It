package com.se2.bopit.domain.data.websocket;

import com.se2.bopit.domain.models.NearbyPayload;

import java.util.ArrayList;
import java.util.List;

public class OutgoingMessage {
    public List<String> to = new ArrayList<>();
    public NearbyPayload data;
}
