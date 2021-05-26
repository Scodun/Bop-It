package com.se2.bopit.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.se2.bopit.domain.interfaces.NetworkDataProvider;
import com.se2.bopit.domain.interfaces.NetworkListener;
import com.se2.bopit.domain.models.NearbyPayload;
import com.se2.bopit.domain.models.User;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.nearby.connection.Strategy.P2P_STAR;

public class NearbyDataProvider implements NetworkDataProvider {

    private static final String SERVICE_ID = "120001";
    private final Context context;
    private NetworkListener listener;
    private final List<User> connectedUsers = new ArrayList<User>();
    private boolean isHost = false;
    private String hostEndpointId = null;
    private final String username;

    private static NearbyDataProvider instance;

    public NearbyDataProvider(Context context, NetworkListener networkListener, String username) {
        this.context = context;
        this.listener = networkListener;
        this.username = username;
    }

    public static NearbyDataProvider getInstance(Context context, NetworkListener networkListener, String username){
        if(instance == null) {
            instance = new NearbyDataProvider(context, networkListener, username);
        }
        return instance;
    }

    @Override
    public void setListener(NetworkListener listener){
        this.listener = listener;
    }

    @Override
    public void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        username, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            isHost=true;
                            sendOnlinePlayers();
                            listener.onStatusChange("Advertising start");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            listener.onError("Advertising error");
                        });
    }

    @Override
    public void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            listener.onStatusChange("Discovering start");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            listener.onError("Discovering error");
                        });
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    if(connectionInfo.isIncomingConnection())
                        connectedUsers.add(new User(endpointId,connectionInfo.getEndpointName()));
                    Nearby.getConnectionsClient(context).acceptConnection(endpointId, mPayloadCallback);

                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            if(isHost)
                                sendOnlinePlayers();
                            else
                                hostEndpointId = endpointId;

                            Nearby.getConnectionsClient(context).stopDiscovery();
                            listener.onStatusChange("Connected");
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            listener.onStatusChange("Connection Refused");
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            listener.onError("Connection Error");
                            break;
                        default:
                            // Unknown status code
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    listener.onStatusChange("Disconnected");
                    connectedUsers.removeIf(e -> e.getId().equals(endpointId));
                    sendOnlinePlayers();
                }
            };

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            final byte[] receivedBytes = payload.asBytes();
            if(receivedBytes!=null){
                Gson gson = new Gson();
                NearbyPayload po = gson.fromJson(new String(receivedBytes), NearbyPayload.class);
                Type type = new TypeToken<List<String>>() {}.getType();
                if(po.getType() == 0)
                    listener.onUserLobbyChange(gson.fromJson(po.getPayload(), type));
            }

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s,
                                            @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                // Do something with is here...
            }
        }
    };


    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    listener.onEndpointDiscovered(endpointId,info.getEndpointName());
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    listener.onStatusChange("Endpoint Lost");
                }
            };

    @Override
    public void connectToEndpoint(String id){
        Nearby.getConnectionsClient(context)
                .requestConnection(username, id, connectionLifecycleCallback)
                .addOnSuccessListener(
                        (Void unused) -> {
                            listener.onStatusChange("Request Connection");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            listener.onError("Request Connection Error");
                        });

    }

    private void sendOnlinePlayers(){
        List<String> users = new ArrayList<>();
        ArrayList<String> deviceNames = new ArrayList<>();
        deviceNames.add(username + " (host)");
        for(User connected:connectedUsers){
            users.add(connected.getId());
            deviceNames.add(connected.getName());
        }
        Gson gson = new Gson();

        listener.onUserLobbyChange(deviceNames);

        Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(0,gson.toJson(deviceNames))).getBytes());
        Nearby.getConnectionsClient(context).sendPayload(users, bytesPayload);
    }

    public void disconnect() {
        if( isHost ) {
            Nearby.getConnectionsClient(context).stopAdvertising();
            Nearby.getConnectionsClient(context).stopAllEndpoints();
            isHost = false;
            connectedUsers.clear();
        } else if( hostEndpointId != null ){
            Nearby.getConnectionsClient(context).disconnectFromEndpoint(hostEndpointId);
            hostEndpointId = null;
        }
        else {
            Nearby.getConnectionsClient(context).stopDiscovery();
        }
        sendOnlinePlayers();
    }

}
