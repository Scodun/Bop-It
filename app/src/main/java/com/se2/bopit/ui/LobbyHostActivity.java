package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.se2.bopit.R;
import com.se2.bopit.data.NearbyDataProvider;
import com.se2.bopit.domain.interfaces.NetworkLobbyListener;
import com.se2.bopit.ui.helpers.CustomToast;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class LobbyHostActivity extends BaseActivity {

    private NearbyDataProvider dp;
    private final ArrayList<String> userItems = new ArrayList<>();
    private ListView lobbyUserList;
    private Context context;
    private static int countdown;
    static ScheduledFuture<?> countdownFuture;

    private ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_host);

        lobbyUserList = findViewById(R.id.userList);
        userAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                userItems);
        lobbyUserList.setAdapter(userAdapter);
        context = this;

        Intent intent = getIntent();
        dp = new NearbyDataProvider(this, networkListener, intent.getStringExtra("username"));
        dp.startAdvertising();
    }

    public void onStartClick(View view) {
        dp.startGameCountDown();
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
            //Listener for Endpoint Discovered
        }

        @Override
        public void onEndpointConnected(String id, ArrayList<String> names) {
            //Listener for Endpoint Connection
        }

        @Override
        public void onUserLobbyChange(ArrayList<String> users) {
            if (users != null) {
                userAdapter.clear();
                userAdapter.addAll(users);
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onGameCountdownStart() {
            LobbyHostActivity.countdown = 3;
            Runnable countdownRunnable = () -> {
                runOnUiThread(
                        () -> {
                            CustomToast.showToast(String.valueOf(LobbyHostActivity.countdown), context);
                            LobbyHostActivity.countdown--;
                            if (LobbyHostActivity.countdown <= 0)
                                countdownFuture.cancel(false);
                        });
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            countdownFuture = executor.scheduleAtFixedRate(countdownRunnable, 0, 1, TimeUnit.SECONDS);
        }

        @Override
        public void onGameStart(ArrayList<String> users) {

        }

        @Override
        public void onGameInformationReceived(String data) {
            Log.i("Network data", data);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dp.disconnect();
    }
}