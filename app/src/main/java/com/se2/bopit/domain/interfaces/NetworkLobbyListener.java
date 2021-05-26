package com.se2.bopit.domain.interfaces;

import java.util.ArrayList;

public interface NetworkLobbyListener {

    void onError(String error);

    void onStatusChange(String status);

    void onGameInformationReceived(String data);

    void onEndpointDiscovered(String id, String name);

    void onEndpointConnected(String id, ArrayList<String> names);

    void onUserLobbyChange(ArrayList<String> users);

    void onGameStart(ArrayList<String> users);

    void onGameCountdownStart();
}
