package com.se2.bopit.domain.data;

import android.util.Log;
import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;

import java.util.ArrayList;
import java.util.List;

public class DataProviderContext {
    static final String TAG = DataProviderContext.class.getSimpleName();

    private DataProviderStrategy dataProvider;
    protected ArrayList<User> users;
//    protected String currentUserId;

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
        Log.d(TAG, "connectToEndpoint: " + endpointId);
        dataProvider.connectToEndpoint(endpointId);
    }

    public void startDiscovery(){
        dataProvider.startDiscovery();
    }

    public void startAdvertising(){
        dataProvider.startAdvertising();
    }

    public void setListener(NetworkGameListener listener){
        Log.d(TAG, "setNetworkGameListener");
        dataProvider.setListener(listener);
    }

    public void setListener(NetworkLobbyListener listener){
        Log.d(TAG, "setNetworkLobbyListener");
        dataProvider.setListener(listener);
    }

    public void startGameCountdown() {
        Log.d(TAG, "startGameCountdown");
        dataProvider.startGameCountdown();
    }

    public void disconnect(){
        dataProvider.disconnect();
    }

    public void sendReadyMessage() {dataProvider.sendReadyMessage(); }


    public void sendReadyAnswer(boolean answer, String username) {dataProvider.sendReadyAnswer(answer, username); }
    public DataProviderStrategy getDataProvider() {
        return dataProvider;
    }

//    public String getUserId() {
//
//        String userId = users.get(0).getId(); // FIXME
//        Log.d(TAG, "getUserId("+users+") => " + userId);
//        return userId;
//    }

    public List<User> getUsers() {
        return users;
    }
}