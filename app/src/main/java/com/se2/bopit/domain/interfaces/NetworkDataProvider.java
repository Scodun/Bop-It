package com.se2.bopit.domain.interfaces;


public interface NetworkDataProvider {

    void connectToEndpoint(String endpointId);

    void startDiscovery();

    void startAdvertising();

    void setListener(NetworkGameListener listener);

    void setListener(NetworkLobbyListener listener);
}
