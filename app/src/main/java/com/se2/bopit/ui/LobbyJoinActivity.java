package com.se2.bopit.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.se2.bopit.R;
import com.se2.bopit.data.WebsocketDataProvider;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.data.DataProviderContext;
import com.se2.bopit.domain.data.NearbyDataProvider;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.helpers.CustomToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class LobbyJoinActivity extends BaseActivity {
    static final String TAG = LobbyJoinActivity.class.getSimpleName();

    private DataProviderContext dataProvider;
    private final ArrayList<String> endpointItems = new ArrayList<>();
    private final ArrayList<String> userItems = new ArrayList<>();
    private String endpointId;
    private Context context;
    static ScheduledFuture<?> countdownFuture;

    private ArrayAdapter<String> endPointAdapter;
    private ArrayAdapter<String> userAdapter;

    private final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_join);
        ListView lobbyUserList;

        ListView openEndpointsList = findViewById(R.id.openEndpointLists);
        lobbyUserList = findViewById(R.id.userList);
        endPointAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                endpointItems);
        userAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                userItems);
        openEndpointsList.setAdapter(endPointAdapter);
        lobbyUserList.setAdapter(userAdapter);
        openEndpointsList.setOnItemClickListener((arg0, arg1, position, arg3) -> dataProvider.connectToEndpoint(endpointId));

        context = this;

        Intent intent = getIntent();
        String networkMode = intent.getStringExtra(HostJoinActivity.NETWORK_MODE);
        switch(networkMode) {
            case "nearby":
            default:
                dataProvider = DataProviderContext.create(new NearbyDataProvider(
                        this, networkListener, intent.getStringExtra(USERNAME)));
                break;
            case "websocket":
                dataProvider = DataProviderContext.create(new WebsocketDataProvider(
                        this, networkListener, intent.getStringExtra(USERNAME)));
                break;
        }
        dataProvider.startDiscovery();
    }

    private final NetworkLobbyListener networkListener = new NetworkLobbyListener() {

        @Override
        public void onError(String error) {
            Log.e("Network", error);
        }

        @Override
        public void onStatusChange(String status) {
            Log.d("Network", status);

        }

        @Override
        public void onEndpointDiscovered(String id, String name) {
            endpointId = id;
            endpointItems.add(name);
            endPointAdapter.notifyDataSetChanged();
        }

        @Override
        public void onEndpointConnected(String id, List<String> names) {
            //Ingore
        }

        @Override
        public void onUserLobbyChange(ArrayList<User> users) {
            if (users != null) {
                userAdapter.clear();
                userAdapter.addAll(users.stream().map(User::getName).collect(Collectors.toList()));
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onGameStart() {
            Log.d(TAG, "onGameStart");
            startGame();
        }

        @Override
        public void onReadyMessageReceived() {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dataProvider.sendReadyAnswer(true, getIntent().getStringExtra(USERNAME));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dataProvider.sendReadyAnswer(false, getIntent().getStringExtra(USERNAME));
                        break;
                    default:
                        Log.d(TAG, "Unknown Message Type");
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you ready?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

        @Override
        public void OnReadyAnswerReceived(boolean answer, String username) {
            //Ingore
        }

        @Override
        public void onGameCountdownStart() {
            AtomicInteger countdown = new AtomicInteger(3);
            Runnable countdownRunnable = () -> {
                runOnUiThread(
                        () -> {
                            CustomToast.showToast(String.valueOf(countdown.get()), context, true);
                            countdown.getAndDecrement();
                            if (countdown.get() <= 0)
                                countdownFuture.cancel(false);
                        });
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            countdownFuture = executor.scheduleAtFixedRate(countdownRunnable, 0, 1, TimeUnit.SECONDS);
        }


        @Override
        public void onGameInformationReceived(String data) {
            Log.i("Network data", data);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataProvider.disconnect();
    }

    void startGame() {
        Log.d(TAG, "startGame");
        startActivity(new Intent(this, GameActivity.class)
                .putExtra(GameActivity.GAME_MODE, GameMode.MULTI_PLAYER_CLIENT));
    }
}