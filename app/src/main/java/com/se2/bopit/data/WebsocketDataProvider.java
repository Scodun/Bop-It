package com.se2.bopit.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.se2.bopit.data.websocket.IncomingMessage;
import com.se2.bopit.data.websocket.OutgoingMessage;
import com.se2.bopit.domain.GameRoundModel;
import com.se2.bopit.domain.responsemodel.ResponseModel;
import com.se2.bopit.domain.data.DataProviderStrategy;
import com.se2.bopit.domain.interfaces.NetworkContextListener;
import com.se2.bopit.domain.interfaces.NetworkGameListener;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.NearbyPayload;
import com.se2.bopit.domain.models.ReadyMessage;
import com.se2.bopit.domain.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;

public class WebsocketDataProvider extends DataProviderStrategy {
    static final String TAG = WebsocketDataProvider.class.getSimpleName();

    static String HOST = "se2-demo.aau.at:53205";

    static final int INT_ADVERTISE_GAME = 100;
    static final int INT_DISCOVER_GAMES = 101;
    static final int INT_JOIN_GAME = 102;
    static final int INT_LEFT_GAME = 103;

    private NetworkLobbyListener lobbyListener;
    private NetworkContextListener contextListener;
    private final ArrayList<User> connectedUsers = new ArrayList<>();
    private boolean isHost = false;
    private String hostEndpointId = null;
    private final String userId = UUID.randomUUID().toString();
    private final String wsUri = "ws://" + HOST + "/game/" + userId;
    private final String username;

    Gson gson = new Gson();
    WebSocketConnection connection;

    public WebsocketDataProvider(Context context, NetworkLobbyListener networkLobbyListener, String username) {
        this.lobbyListener = networkLobbyListener;
        this.username = username;
        Log.d(TAG, "created for username: " + username);
    }

    @Override
    public void setListener(NetworkLobbyListener listener) {
        this.lobbyListener = listener;
    }

    @Override
    public void setListener(NetworkContextListener listener) {
        this.contextListener = listener;
    }

    void connect(Runnable onOpen) {
        connection = new WebSocketConnection();
        try {
            connection.connect(wsUri, new WebSocketConnectionHandler() {
                @Override
                public void onConnect(ConnectionResponse response) {
                    Log.d(TAG, "onConnect: " + response.protocol);
                }

                @Override
                public void onOpen() {
                    Log.d(TAG, "onOpen");

                    onOpen.run();
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "onClose " + code + " " + reason);
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "onMessage: " + message);
                    IncomingMessage incoming = gson.fromJson(message, IncomingMessage.class);
                    onPayloadReceived(incoming.from, incoming.data);
                }
            });

        } catch (WebSocketException ex) {
            Log.e(TAG, "Error", ex);
        }
        Log.d(TAG, "connected");
    }

    @Override
    public void startAdvertising() {
        isHost = true;
        connect(() -> {
            sendSystemPayload(new NearbyPayload(INT_ADVERTISE_GAME, username));
        });
    }

    @Override
    public void startDiscovery() {
        isHost = false;

        connect(() -> {
            sendSystemPayload(new NearbyPayload(INT_DISCOVER_GAMES, username));
            lobbyListener.onStatusChange("Discovering start");
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    void onPayloadReceived(String endpointId, NearbyPayload payload) {
        Log.d(TAG, "onPayloadReceived: " + endpointId + " " + payload);
        Type type;

        switch (payload.getType()) {
            case NearbyPayload.USER_LOBBY_UPDATE:
                type = new TypeToken<List<User>>() {}.getType();
                List<User> users = unwrapPayload(payload, type);
                Log.d(TAG, "lobby changed: " + users);
                lobbyListener.onUserLobbyChange(new ArrayList<>(users));
                break;
            case NearbyPayload.GAME_COUNT_DOWN:
                lobbyListener.onGameCountdownStart();
                break;
            case NearbyPayload.GAME_START:
                type =  new TypeToken<List<User>>() {}.getType();
                contextListener.onGameStart(unwrapPayload(payload, type));
                lobbyListener.onGameStart();
                break;
            case 7:
                lobbyListener.onReadyMessageReceived();
                break;
            case 8:
                ReadyMessage msg = unwrapPayload(payload, ReadyMessage.class);
                lobbyListener.OnReadyAnswerReceived(msg.answer, msg.username);
                break;

            case NearbyPayload.READY_TO_START:
                gameEngineServer.readyToStart(unwrapPayload(payload, String.class));
                break;
            case NearbyPayload.START_NEW_ROUND:
                gameEngineClient.startNewGame(unwrapPayload(payload, GameRoundModel.class));
                break;
            case NearbyPayload.SEND_ROUND_RESULT:
                gameEngineServer.sendGameResult(endpointId, unwrapPayload(payload, Boolean.class), null);
                break;
            case NearbyPayload.NOTIFY_ROUND_RESULT:
                gameEngineClient.notifyGameResult(false, null, unwrapPayload(payload, User.class));
                break;
            case NearbyPayload.STOP_CURRENT_GAME:
                gameEngineServer.stopCurrentGame(endpointId);
                break;
            case NearbyPayload.NOTIFY_GAME_OVER:
                gameEngineClient.stopCurrentGame();
                break;
            case NearbyPayload.SET_CLIENT_CHEATED:
                gameEngineServer.setClientCheated(endpointId);
                break;
            case NearbyPayload.DETECT_CHEATING:
                gameEngineServer.detectCheating(endpointId);
                break;
            case NearbyPayload.CHEATER_DETECTED:
                gameEngineClient.cheaterDetected(unwrapPayload(payload, String.class));
                break;

                // internal messages
            case INT_ADVERTISE_GAME: // create
                connectedUsers.add(new User(userId, username));
                sendOnlinePlayers();
                lobbyListener.onStatusChange("Advertising start");
                break;
            case INT_DISCOVER_GAMES: // discover
                // TODO
                type =  new TypeToken<Map<String,String>>() {}.getType();
                Map<String,String> lobbies = unwrapPayload(payload, type);
                lobbies.forEach((i, n) -> lobbyListener.onEndpointDiscovered(i, n));
                break;
            case INT_JOIN_GAME:
                // TODO
                if(isHost) {
                    connectedUsers.add(new User(endpointId, payload.getPayload()));
                    sendOnlinePlayers();
                }
                break;
            case INT_LEFT_GAME:
                // TODO
                if(isHost) {
                    connectedUsers.removeIf(u -> u.getId().equals(endpointId));
                    sendOnlinePlayers();
                    //lobbyListener.onStatusChange("Disconnected");
                }
                break;

            default:
                Log.e(TAG, "Unknown payload [from " + endpointId + "]: " + payload);
        }
    }



//    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
//            new EndpointDiscoveryCallback() {
//                @Override
//                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
//                    lobbyListener.onEndpointDiscovered(endpointId, info.getEndpointName());
//                }
//
//                @Override
//                public void onEndpointLost(String endpointId) {
//                    lobbyListener.onStatusChange("Endpoint Lost");
//                }
//            };

    @Override
    public void connectToEndpoint(String id) {
        sendSystemPayload(new NearbyPayload(INT_JOIN_GAME, id));
        lobbyListener.onStatusChange("Request Connection");

//        Nearby.getConnectionsClient(context)
//                .requestConnection(username, id, connectionLifecycleCallback)
//                .addOnSuccessListener(
//                        (Void unused) -> {
//                            lobbyListener.onStatusChange("Request Connection");
//                        })
//                .addOnFailureListener(
//                        (Exception e) -> {
//                            lobbyListener.onError("Request Connection Error");
//                        });
//
    }

    private void sendOnlinePlayers() {
        lobbyListener.onUserLobbyChange(connectedUsers);
        sendPayload(getConnectedUserIds(),
                new NearbyPayload(NearbyPayload.USER_LOBBY_UPDATE, gson.toJson(connectedUsers)));

        //Payload bytesPayload = Payload.fromBytes(gson.toJson(new NearbyPayload(0, gson.toJson(connectedUsers))).getBytes());
        //Nearby.getConnectionsClient(context).sendPayload(getConnectedUserIds(), bytesPayload);
    }

    public void disconnect() {
        if (isHost) {
            //Nearby.getConnectionsClient(context).stopAdvertising();
            //Nearby.getConnectionsClient(context).stopAllEndpoints();
            connection.sendClose();
            isHost = false;
            connectedUsers.clear();
        } else if (hostEndpointId != null) {
            //Nearby.getConnectionsClient(context).disconnectFromEndpoint(hostEndpointId);
            connection.sendClose();
            hostEndpointId = null;
        } else {
            //Nearby.getConnectionsClient(context).stopDiscovery();
            connection.sendClose();
        }
        sendOnlinePlayers();
    }

    @Override
    public void startGameCountdown() {
        if (isHost) {
            sendPayload(getConnectedUserIds(), wrapPayload(NearbyPayload.GAME_COUNT_DOWN));
            lobbyListener.onGameCountdownStart();

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            sendPayload(getConnectedUserIds(),
                                    wrapPayload(NearbyPayload.GAME_START, connectedUsers));
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
            List<String> to = getConnectedUserIds();
            if(!to.isEmpty()) {
                sendPayload(to, wrapPayload(7));
            }
        }
    }
    
    @Override
    public void sendReadyAnswer(boolean answer, String username) {
        if (!isHost) {
            ReadyMessage ready = new ReadyMessage();
            ready.answer = answer;
            ready.username = username;
            sendPayload(hostEndpointId, wrapPayload(8, ready));
        }
    }

    List<String> getConnectedUserIds() {
        if (isHost) {
            List<String> users = new ArrayList<>();
            for (User connected : connectedUsers) {
                if(!connected.getId().equals(userId))
                    users.add(connected.getId());
            }
            return users;
        }
        return new ArrayList<>();
    }

    NearbyPayload wrapPayload(int type, Object data) {
        return new NearbyPayload(type, gson.toJson(data));
    }

    NearbyPayload wrapPayload(int type) {
        return new NearbyPayload(type, null);
    }

    <T> T unwrapPayload(NearbyPayload payload, Class<T> type) {
        return gson.fromJson(payload.getPayload(), type);
    }

    <T> T unwrapPayload(NearbyPayload payload, Type type) {
        return gson.fromJson(payload.getPayload(), type);
    }

    void sendSystemPayload(NearbyPayload payload) {
        sendPayload(Collections.emptyList(), payload);
    }

    void sendPayload(String to, NearbyPayload payload) {
        sendPayload(Collections.singletonList(to), payload);
    }

    void sendPayload(List<String> to, NearbyPayload payload) {
        if(connection.isConnected()) {
            OutgoingMessage msg = new OutgoingMessage();
            msg.to.addAll(to);
            msg.data = payload;
            connection.sendMessage(gson.toJson(msg));
        } else {
            Log.d(TAG, "attempt to send via disconnected connection! to: "
                    + to + "; payload: " + payload);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void readyToStart(String userId) {
        if(isHost) {
            Log.d(TAG, "Host readyToStart");
            gameEngineServer.readyToStart(userId);
        } else {
            Log.d(TAG, "Client " + userId + " readyToStart");
            sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.READY_TO_START, userId));
        }
    }

    @Override
    public void startNewGame(GameRoundModel roundModel) {
        if(isHost) {
            Log.d(TAG, "Broadcast startNewGame " + roundModel);
            gameEngineClient.startNewGame(roundModel);
            sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.START_NEW_ROUND, roundModel));
        } else {
            Log.w(TAG, "Unexpected call startNewGame from client!");
        }
    }

    @Override
    public void sendGameResult(String userId, boolean result, ResponseModel responseModel) {
        if(isHost) {
            Log.d(TAG, "Host sendGameResult: " + userId + ": " + result);
            gameEngineServer.sendGameResult(userId, result, responseModel);
        } else {
            Log.d(TAG, "Client sendGameResult: " + userId + ": " + result);
            sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.SEND_ROUND_RESULT, result));
        }
    }

    @Override
    public void notifyGameResult(boolean result, ResponseModel responseModel, User user) {
        if(isHost) {
            Log.d(TAG, "Broadcast notifyGameResult: " + result);
            gameEngineClient.notifyGameResult(result, responseModel, user);
            sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.NOTIFY_ROUND_RESULT, user));
        } else {
            Log.w(TAG, "Unexpected call notifyGameResult from client!");
        }
    }

    @Override
    public void stopCurrentGame(String userId) {
        if(isHost) {
            Log.d(TAG, "Host stopCurrentGame: " + userId);
            gameEngineServer.stopCurrentGame(userId);
        } else {
            Log.d(TAG, "Client stopCurrentGame: " + userId);
            sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.STOP_CURRENT_GAME, userId));
        }
    }

    @Override
    public void notifyGameOver() {
        if(isHost) {
            Log.d(TAG, "Broadcast game over");
            sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.NOTIFY_GAME_OVER));
            gameEngineClient.stopCurrentGame();
        }
    }

    @Override
    public User[] getRoundResult() {
        
        // TODO
        return new User[0];
    }

    @Override
    public void setClientCheated(String userId) {
        if (isHost) {
            gameEngineServer.setClientCheated(userId);
        }else {
            sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.SET_CLIENT_CHEATED, userId));
        }
    }

    @Override
    public void detectCheating() {
        if (isHost) {
            gameEngineServer.detectCheating(userId);
        }else {
            sendPayload(hostEndpointId,
                    wrapPayload(NearbyPayload.DETECT_CHEATING));
        }
    }

    @Override
    public String getUserId() {
        return userId;
    }


    @Override
    public void cheaterDetected(String cheaterId) {
        if(isHost) {
            Log.d(TAG, "Broadcast cheater detected");
            sendPayload(getConnectedUserIds(),
                    wrapPayload(NearbyPayload.CHEATER_DETECTED, cheaterId));
        }
    }
}
