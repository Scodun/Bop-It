package com.se2.bopit.domain.data;

import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;

import java.util.ArrayList;

public class DataProviderContext {
    private DataProviderStrategy dataProvider;
    protected ArrayList<User> users;

    public DataProviderContext(DataProviderStrategy dataProvider){
        this.dataProvider = dataProvider;
        NetworkContextListener networkListener = u -> users = u;
        this.dataProvider.setListener(networkListener);
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