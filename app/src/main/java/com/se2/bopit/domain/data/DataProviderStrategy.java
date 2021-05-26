package com.se2.bopit.domain.data;

import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;

public abstract class DataProviderStrategy {

    public abstract void connectToEndpoint(String endpointId);

    public abstract void startDiscovery();

    public abstract void startAdvertising();

    public abstract void setListener(NetworkGameListener listener);

    public abstract void setListener(NetworkLobbyListener listener);

    public abstract void startGameCountdown();

    public abstract void disconnect();

}
