package com.se2.bopit.domain.data;

import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataProviderContext {
    private DataProviderStrategy dataProvider;
    protected ArrayList<User> users;
    
    private static DataProviderContext context;

    private DataProviderContext(DataProviderStrategy dataProvider){
        this.dataProvider = dataProvider;
        NetworkContextListener networkListener = u -> users = u;
        this.dataProvider.setListener(networkListener);
    }
    
    public static DataProviderContext create(DataProviderStrategy strategy) {
        context = new DataProviderContext(strategy);
        return context;
    }

    public static DataProviderContext getContext() {
        return context;
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


    public DataProviderStrategy getDataProvider() {
        return dataProvider;
    }

    public String getUserId() {
        return users.get(0).getId(); // FIXME
    }

    public List<User> getUsers() {
        return users;
    }
}