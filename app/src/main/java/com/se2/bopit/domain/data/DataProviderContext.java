package com.se2.bopit.domain.data;

import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;

public class DataProviderContext {
    private DataProviderStrategy dataProvider;

    public DataProviderContext(DataProviderStrategy dataProvider){
        this.dataProvider = dataProvider;
    }

    public void setDataProvider(DataProviderStrategy dataProvider){
        this.dataProvider = dataProvider;
    }

    public void connectToEndpoint(String endpointId){
        dataProvider.connectToEndpoint(endpointId);
    }

    public void startDiscovery(){
        dataProvider.startDiscovery();
    }

    public void startAdvertising(){
        dataProvider.startAdvertising();
    }

    public void setListener(NetworkGameListener listener){
        dataProvider.setListener(listener);
    }

    public void setListener(NetworkLobbyListener listener){
        dataProvider.setListener(listener);
    }

    public void startGameCountdown(){
        dataProvider.startGameCountdown();
    }

    public void disconnect(){
        dataProvider.disconnect();
    }

}