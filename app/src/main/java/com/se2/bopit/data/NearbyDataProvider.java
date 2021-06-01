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
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.ResponseModel;
import com.se2.bopit.domain.data.DataProviderStrategy;
import com.se2.bopit.domain.engine.GameEngineServer;
import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.NearbyPayload;
import com.se2.bopit.domain.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.nearby.connection.Strategy.P2P_STAR;

public class NearbyDataProvider extends DataProviderStrategy {

    private static final String SERVICE_ID = "120001";
    private final Context context;
    private NetworkLobbyListener lobbyListener;
    private NetworkGameListener gameListener;
    private NetworkContextListener contextListener;
    private final ArrayList<User> connectedUsers = new ArrayList<User>();
    private boolean isHost = false;
    private String hostEndpointId = null;
    private final String username;

    Gson gson = new Gson();

    private static NearbyDataProvider instance;

    public NearbyDataProvider(Context context, NetworkLobbyListener networkLobbyListener, String username) {
        this.context = context;
        this.lobbyListener = networkLobbyListener;
        this.username = username;
    }

    public NearbyDataProvider(Context context, NetworkGameListener networkGameListener, String username) {
        this.context = context;
        this.gameListener = networkGameListener;
        this.username = username;
    }

    public static NearbyDataProvider getInstance(Context context, NetworkLobbyListener networkLobbyListener, String username) {
        if (instance == null) {
            instance = new NearbyDataProvider(context, networkLobbyListener, username);
        }
        return instance;
    }

    public static NearbyDataProvider getInstance(Context context, NetworkGameListener networkGameListener, String username) {
        if (instance == null) {
            instance = new NearbyDataProvider(context, networkGameListener, username);
        }
        return instance;
    }

    @Override
    public void setListener(NetworkLobbyListener listener) {
        this.lobbyListener = listener;
    }

    @Override
    public void setListener(NetworkGameListener listener) {
        this.gameListener = listener;
    }

    @Override
    public void setListener(NetworkContextListener listener) {
        this.contextListener = listener;
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
                            isHost = true;
                            connectedUsers.add(new User("0",username));
                            sendOnlinePlayers();
                            lobbyListener.onStatusChange("Advertising start");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            lobbyListener.onError("Advertising error");
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
                            lobbyListener.onStatusChange("Discovering start");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            lobbyListener.onError("Discovering error");
                        });
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    if (connectionInfo.isIncomingConnection())
                        connectedUsers.add(new User(endpointId, connectionInfo.getEndpointName()));
                    Nearby.getConnectionsClient(context).acceptConnection(endpointId, mPayloadCallback);

                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            if (isHost)
                                sendOnlinePlayers();
                            else
                                hostEndpointId = endpointId;

                            Nearby.getConnectionsClient(context).stopDiscovery();
                            lobbyListener.onStatusChange("Connected");
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            lobbyListener.onStatusChange("Connection Refused");
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            lobbyListener.onError("Connection Error");
                            break;
                        default:
                            // Unknown status code
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDisconnected(String endpointId) {
                    lobbyListener.onStatusChange("Disconnected");
                    connectedUsers.removeIf(e -> e.getId().equals(endpointId));
                    sendOnlinePlayers();
                }
            };

    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            final byte[] receivedBytes = payload.asBytes();
            if (receivedBytes != null) {
                Gson gson = new Gson();
                NearbyPayload po = gson.fromJson(new String(receivedBytes), NearbyPayload.class);
                Type type = new TypeToken<List<User>>() {
                }.getType();

                switch (po.getType()) {
                    case 0:
                        lobbyListener.onUserLobbyChange(gson.fromJson(po.getPayload(), type));
                        break;
                    case 1:
                        lobbyListener.onGameCountdownStart();
                        break;
                    case 2:
                        contextListener.onGameStart(gson.fromJson(po.getPayload(), type));
                        lobbyListener.onGameStart();
                        break;

                    default:
                        Log.e("NearbyDataProvider", "Unknown payload: " + po);
                }
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
                    lobbyListener.onEndpointDiscovered(endpointId, info.getEndpointName());
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    lobbyListener.onStatusChange("Endpoint Lost");
                }
            };

    @Override
    public void connectToEndpoint(String id) {
        Nearby.getConnectionsClient(context)
                .requestConnection(username, id, connectionLifecycleCallback)
                .addOnSuccessListener(
                        (Void unused) -> {
                            lobbyListener.onStatusChange("Request Connection");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            lobbyListener.onError("Request Connection Error");
                        });

    }

    private void sendOnlinePlayers() {
        Gson gson = new Gson();
        lobbyListener.onUserLobbyChange(connectedUsers);

        Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(0, gson.toJson(connectedUsers))).getBytes());
        Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);
    }

    public void disconnect() {
        if (isHost) {
            Nearby.getConnectionsClient(context).stopAdvertising();
            Nearby.getConnectionsClient(context).stopAllEndpoints();
            isHost = false;
            connectedUsers.clear();
        } else if (hostEndpointId != null) {
            Nearby.getConnectionsClient(context).disconnectFromEndpoint(hostEndpointId);
            hostEndpointId = null;
        } else {
            Nearby.getConnectionsClient(context).stopDiscovery();
        }
        sendOnlinePlayers();
    }

    @Override
    public void startGameCountdown() {
        if (isHost) {

            Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(1, null)).getBytes());
            Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);
            lobbyListener.onGameCountdownStart();

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(2, gson.toJson(connectedUsers))).getBytes());
                            Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);
                            contextListener.onGameStart(connectedUsers);
                            lobbyListener.onGameStart();
                        }
                    },
                    3000
            );
        }
    }

    private ArrayList<String> getConnectedUserIds() {
        if (isHost) {
            ArrayList<String> users = new ArrayList<>();
            for (User connected : connectedUsers) {
                if(!connected.getId().equals("0"))
                    users.add(connected.getId());
            }
            return users;
        }
        return new ArrayList<String>() {
        };
    }

    ConnectionsClient getConnectionsClient() {
        return Nearby.getConnectionsClient(context);
    }

    Payload wrapPayload(int type, Object data) {
        return Payload.fromBytes(gson.toJson(new NearbyPayload(type, gson.toJson(data))).getBytes());
    }

    Payload wrapPayload(int type) {
        return Payload.fromBytes(gson.toJson(new NearbyPayload(type, null)).getBytes());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void readyToStart(String userId) {
        if(isHost) {
            gameEngineServer.readyToStart(userId);
        } else {
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.READY_TO_START, userId));
        }
    }

    @Override
    public void startNewGame(GameRoundModel roundModel) {
        if(isHost) {
            gameEngineClient.startNewGame(roundModel);
        } else {
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.START_NEW_ROUND, roundModel));
        }
    }

    @Override
    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        if(isHost) {
            gameEngineServer.sendGameResult(userId, result, responseModel);
        } else {
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.SEND_ROUND_RESULT, result));
        }
    }

    @Override
    public void notifyGameResult(boolean result, ResponseModel responseModel) {
        if(isHost) {
           gameEngineClient.notifyGameResult(result, responseModel);
        } else {
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.NOTIFY_ROUND_RESULT, result));
        }
    }

    @Override
    public void stopCurrentGame(String userId) {
        if(isHost) {
            gameEngineServer.stopCurrentGame(userId);
        } else {
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.STOP_CURRENT_GAME, userId));
        }
    }

    @Override
    public User[] getRoundResult() {
        // TODO
        return new User[0];
    }

}