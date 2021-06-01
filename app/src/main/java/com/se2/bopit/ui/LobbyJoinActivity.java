package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.se2.bopit.R;
import com.se2.bopit.data.NearbyDataProvider;
import com.se2.bopit.domain.data.DataProviderContext;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.helpers.CustomToast;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class LobbyJoinActivity extends BaseActivity {

    private DataProviderContext dataProvider;
    private ListView openEndpointsList;
    private final ArrayList<String> endpointItems = new ArrayList<>();
    private final ArrayList<String> userItems = new ArrayList<>();
    private String endpointId;
    private ListView lobbyUserList;
    private Context context;
    private static int countdown;
    static ScheduledFuture<?> countdownFuture;

    private ArrayAdapter<String> endPointAdapter;
    private ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_join);

        openEndpointsList = findViewById(R.id.openEndpointLists);
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
        dataProvider = DataProviderContext.create(new NearbyDataProvider(this, networkListener, intent.getStringExtra("username")));
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
        public void onEndpointConnected(String id, ArrayList<String> names) {

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

        }

        @Override
        public void onGameCountdownStart() {
            LobbyJoinActivity.countdown = 3;
            Runnable countdownRunnable = () -> {
                runOnUiThread(
                        () -> {
                            CustomToast.showToast(String.valueOf(LobbyJoinActivity.countdown), context);
                            LobbyJoinActivity.countdown--;
                            if (LobbyJoinActivity.countdown <= 0)
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
}