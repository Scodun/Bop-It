package com.se2.bopit.domain.data;

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
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.NearbyPayload;
import com.se2.bopit.domain.models.ReadyMessage;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.domain.responsemodel.ResponseModel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.nearby.connection.Strategy.P2P_STAR;

public class NearbyDataProvider extends DataProviderStrategy {
    static final String TAG = NearbyDataProvider.class.getSimpleName();

    private static final String SERVICE_ID = "120001";
    private final Context context;
    private NetworkLobbyListener lobbyListener;
    private NetworkContextListener contextListener;
    private final ArrayList<User> connectedUsers = new ArrayList<>();
    private boolean isHost = false;
    private String hostEndpointId = null;
    private String userId = null;
    private final String username;

    Gson gson = new Gson();

    private static NearbyDataProvider instance;

    public NearbyDataProvider(Context context, NetworkLobbyListener networkLobbyListener, String username) {
        this.context = context;
        this.lobbyListener = networkLobbyListener;
        this.username = username;
    }

    public NearbyDataProvider(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public static NearbyDataProvider getInstance(Context context, NetworkLobbyListener networkLobbyListener, String username) {
        if (instance == null) {
            instance = new NearbyDataProvider(context, networkLobbyListener, username);
        }
        return instance;
    }

    public static NearbyDataProvider getInstance(Context context, String username) {
        if (instance == null) {
            instance = new NearbyDataProvider(context, username);
        }
        return instance;
    }

    @Override
    public void setListener(NetworkLobbyListener listener) {
        this.lobbyListener = listener;
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
                            userId = "0";
                            connectedUsers.add(new User(userId, username));
                            sendOnlinePlayers();
                            lobbyListener.onStatusChange("Advertising start");
                        })
                .addOnFailureListener(
                        (Exception e) -> lobbyListener.onError("Advertising error"));
    }

    @Override
    public void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(P2P_STAR).build();
        Nearby.getConnectionsClient(context)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> lobbyListener.onStatusChange("Discovering start"))
                .addOnFailureListener(
                        (Exception e) -> lobbyListener.onError("Discovering error"));
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
                            Log.w(TAG, "Unknown status code");
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
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
            Log.d(TAG, "onPayloadReceived: " + endpointId);
            final byte[] receivedBytes = payload.asBytes();
            if (receivedBytes != null) {
                NearbyPayload po = gson.fromJson(new String(receivedBytes), NearbyPayload.class);
                Log.d(TAG, "parsing payload: " + po);
                Type type = new TypeToken<List<User>>() {
                }.getType();

                switch (po.getType()) {
                    case 0:
                        ArrayList<User> users = gson.fromJson(po.getPayload(), type);
                        Log.d(TAG, "lobby changed: " + users);
                        if (userId == null) {
                            users.stream()
                                    .filter(u -> u.getName().equals(username))
                                    .map(User::getId)
                                    .findFirst()
                                    .ifPresent(id -> {
                                        userId = id;
                                        Log.d(TAG, "my userId: " + userId);
                                    });
                        }
                        lobbyListener.onUserLobbyChange(users);
                        break;
                    case 1:
                        lobbyListener.onGameCountdownStart();
                        break;
                    case 2:
                        contextListener.onGameStart(gson.fromJson(po.getPayload(), type));
                        lobbyListener.onGameStart();
                        break;
                    case 7:
                        lobbyListener.onReadyMessageReceived();
                        break;
                    case 8:
                        ReadyMessage msg = gson.fromJson(po.getPayload(), ReadyMessage.class);
                        lobbyListener.onReadyAnswerReceived(msg.isAnswer(), msg.getUsername());
                        break;

                    case NearbyPayload.READY_TO_START:
                        gameEngineServer.readyToStart(gson.fromJson(po.getPayload(), String.class));
                        break;
                    case NearbyPayload.START_NEW_ROUND:
                        gameEngineClient.startNewGame(gson.fromJson(po.getPayload(), GameRoundModel.class));
                        break;
                    case NearbyPayload.SEND_ROUND_RESULT:
                        gameEngineServer.sendGameResult(endpointId, gson.fromJson(po.getPayload(), Boolean.class), null);
                        break;
                    case NearbyPayload.NOTIFY_ROUND_RESULT:
                        gameEngineClient.notifyGameResult(false, null, gson.fromJson(po.getPayload(), User.class));
                        break;
                    case NearbyPayload.STOP_CURRENT_GAME:
                        gameEngineServer.stopCurrentGame(endpointId);
                        break;
                    case NearbyPayload.NOTIFY_GAME_OVER:
                        gameEngineClient.stopCurrentGame(gson.fromJson(po.getPayload(), type));
                        break;
                    case NearbyPayload.SET_CLIENT_CHEATED:
                        gameEngineServer.setClientCheated(endpointId);
                        break;
                    case NearbyPayload.DETECT_CHEATING:
                        gameEngineServer.detectCheating(endpointId);
                        break;
                    case NearbyPayload.CHEATER_DETECTED:
                        gameEngineClient.cheaterDetected(gson.fromJson(po.getPayload(), String.class));
                        break;

                    default:
                        Log.e(TAG, "Unknown payload [from " + endpointId + "]: " + po);
                }
            } else {
                Log.d(TAG, "received empty payload...");
            }
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s,
                                            @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                // Do something with is here...
                Log.d(TAG, "onPayloadTransferUpdate: " + s);
            } else {
                Log.d(TAG, "onPayloadTransferUpdate: status=" + payloadTransferUpdate.getStatus()
                        + " " + payloadTransferUpdate.getBytesTransferred() + "/" + payloadTransferUpdate.getTotalBytes() + " bytes");
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
                        (Void unused) -> lobbyListener.onStatusChange("Request Connection"))
                .addOnFailureListener(
                        (Exception e) -> lobbyListener.onError("Request Connection Error"));

    }

    private void sendOnlinePlayers() {
        lobbyListener.onUserLobbyChange(connectedUsers);

        Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(0, gson.toJson(connectedUsers))).getBytes());
        Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);
    }

    @Override
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
                    6000
            );
        }
    }

    @Override
    public void sendReadyMessage() {
        if (isHost) {
            Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(7, null)).getBytes());
            Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);


        }
    }

    @Override
    public void sendReadyAnswer(boolean answer, String username) {
        if (!isHost) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("answer", answer);
                obj.put("username", username);
                Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(8, obj.toString())).getBytes());
                Nearby.getConnectionsClient(context).sendPayload(hostEndpointId, bytesPayload);
            } catch (Exception ex) {
                Log.d(TAG, "Error When ReadAnswer");
            }


        }
    }

    private ArrayList<String> getConnectedUserIds() {
        if (isHost) {
            ArrayList<String> users = new ArrayList<>();
            for (User connected : connectedUsers) {
                if (!connected.getId().equals("0"))
                    users.add(connected.getId());
            }
            return users;
        }
        return new ArrayList<>();
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
        if (isHost) {
            Log.d(TAG, "Host readyToStart");
            gameEngineServer.readyToStart(userId);
        } else {
            Log.d(TAG, "Client " + userId + " readyToStart");
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.READY_TO_START, userId));
        }
    }

    @Override
    public void startNewGame(GameRoundModel roundModel) {
        if (isHost) {
            Log.d(TAG, "Broadcast startNewGame " + roundModel);
            gameEngineClient.startNewGame(roundModel);
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.START_NEW_ROUND, roundModel));
        } else {
            Log.w(TAG, "Unexpected call startNewGame from client!");
        }
    }

    @Override
    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        if (isHost) {
            Log.d(TAG, "Host sendGameResult: " + userId + ": " + result);
            gameEngineServer.sendGameResult(userId, result, responseModel);
        } else {
            Log.d(TAG, "Client sendGameResult: " + userId + ": " + result);
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.SEND_ROUND_RESULT, result));
        }
    }

    @Override
    public void notifyGameResult(boolean result, ResponseModel responseModel, User user) {
        if (isHost) {
            Log.d(TAG, "Broadcast notifyGameResult: " + result);
            gameEngineClient.notifyGameResult(result, responseModel, user);
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.NOTIFY_ROUND_RESULT, user));
        } else {
            Log.w(TAG, "Unexpected call notifyGameResult from client!");
        }
    }

    @Override
    public void stopCurrentGame(String userId) {
        if (isHost) {
            Log.d(TAG, "Host stopCurrentGame: " + userId);
            gameEngineServer.stopCurrentGame(userId);
        } else {
            Log.d(TAG, "Client stopCurrentGame: " + userId);
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.STOP_CURRENT_GAME, userId));
        }
    }

    @Override
    public void notifyGameOver() {
        if (isHost) {
            DataProviderContext dpc = DataProviderContext.getContext();
            Log.d(TAG, "Broadcast game over");
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.NOTIFY_GAME_OVER, dpc.getUsers()));
            gameEngineClient.stopCurrentGame(dpc.getUsers());
        }
    }

    @Override
    public User[] getRoundResult() {
        return new User[0];
    }

    @Override
    public void setClientCheated(String userId) {
        if (isHost) {
            gameEngineServer.setClientCheated(userId);
        } else {
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.SET_CLIENT_CHEATED, userId));
        }
    }

    @Override
    public void detectCheating() {
        if (isHost) {
            gameEngineServer.detectCheating(userId);
        } else {
            getConnectionsClient().sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.DETECT_CHEATING));
        }
    }

    @Override
    public String getUserId() {
        return userId;
    }


    @Override
    public void cheaterDetected(String cheaterId) {
        if (isHost) {
            Log.d(TAG, "Broadcast cheater detected");
            getConnectionsClient().sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.CHEATER_DETECTED, cheaterId));
        }
    }
}
