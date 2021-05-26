package com.se2.bopit.domain.interfaces;

import com.se2.bopit.domain.models.User;

import java.util.ArrayList;

public interface NetworkLobbyListener {

    void onError(String error);

    void onStatusChange(String status);

    void onGameInformationReceived(String data);

    void onEndpointDiscovered(String id, String name);

    void onEndpointConnected(String id, ArrayList<String> names);

    void onUserLobbyChange(ArrayList<User> users);

    void onGameStart();

    void onGameCountdownStart();
}
