package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public interface NetworkLobbyListener {

    void onError(String error);

    void onStatusChange(String status);

    void onGameInformationReceived(String data);

    void onEndpointDiscovered(String id, String name);

    void onEndpointConnected(String id, List<String> names);

    void onUserLobbyChange(List<User> users);

    void onGameStart();

    void onGameCountdownStart();
}
